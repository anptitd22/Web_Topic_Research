package com.example.researcharticles.component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.researcharticles.helper.JwtTokenHelper;
import com.example.researcharticles.service.AdminDetailServiceImpl;
import com.example.researcharticles.service.UserDetailServiceImpl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.internal.Pair;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter{
    @Value("${api.prefix:/api}")
    private String apiPrefix;

    private final JwtTokenHelper jwtTokenUtil;
    private final UserDetailServiceImpl userDetailService;
    private final AdminDetailServiceImpl adminDetailService;

    @Override
    protected void doFilterInternal (
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try{
            if (isBypassToken(request)){
                filterChain.doFilter(request, response);
                return;
            }

            // final String authHeader = request.getHeader("Authorization");
            // final String authType = request.getHeader("X-Auth-Type");

            final String token = resolveToken(request);
            if (token == null || token.isEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return;
            }
            // log.info(token);
            final String subject = jwtTokenUtil.extractSubject(token);
            final String role = jwtTokenUtil.extractRole(token);

            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = null;
                if ("user".equalsIgnoreCase(role)) {
                    userDetails = userDetailService.loadUserByUsername(subject);
                } else {
                    userDetails = adminDetailService.loadUserByUsername(subject);
                }

                if (jwtTokenUtil.validateToken(token, userDetails)){
                    List<GrantedAuthority> authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())
                    );
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    authorities
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response); //enable bypass
        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "ForgotToken expired");
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
        }
    }

    private boolean isBypassToken (@NonNull HttpServletRequest request){
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("/%s/actuator/health", apiPrefix), "GET"),

                // Swagger
                Pair.of("/api-docs", "GET"),
                Pair.of("/api-docs/**", "GET"),
                Pair.of("/swagger-resources", "GET"),
                Pair.of("/swagger-resources/**", "GET"),
                Pair.of("/configuration/ui", "GET"),
                Pair.of("/configuration/security", "GET"),
                Pair.of("/swagger-ui/**", "GET"),
                Pair.of("/swagger-ui.html", "GET"),
                Pair.of("/swagger-ui/index.html", "GET"),

                Pair.of(String.format("%s/auth/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/auth/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/articles", apiPrefix), "GET"),
                Pair.of(String.format("%s/articles/search/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/tags/**", apiPrefix), "GET"),
                Pair.of(String.format("%s/tags", apiPrefix), "GET"),
                Pair.of(String.format("%s/admin/login", apiPrefix), "POST")
        );
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();
        for (Pair<String, String > bypassToken : bypassTokens){
            String tokenPath = bypassToken.getLeft();
            String tokenMethod = bypassToken.getRight();

            if (!requestMethod.equalsIgnoreCase(tokenMethod)) continue;

            if (tokenPath.contains("**")) {
                // Convert wildcard to regex
                String regexPath = tokenPath.replace("**", ".*");
                if (requestPath.matches(regexPath)) {
                    return true;
                }
            } else if (requestPath.equals(tokenPath)) {
                return true;
            }
        }
        return false;
    }

    private String resolveToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) return auth.substring(7);
        return com.example.researcharticles.util.CookieUtil
                .getCookieValue(request, "access_token")
                .orElse(null);
    }
}
