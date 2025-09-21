package com.example.researcharticles.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.researcharticles.dto.request.LoginRequest;
import com.example.researcharticles.dto.request.RegisterRequest;
import com.example.researcharticles.dto.response.ObjectResponse;
import com.example.researcharticles.dto.response.UserResponse;
import com.example.researcharticles.helper.ValidationHelper;
import com.example.researcharticles.model.User;
import com.example.researcharticles.service.UserServiceImpl;
import com.example.researcharticles.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<ObjectResponse<?>> createUser(
        @RequestBody RegisterRequest registerDTO,
        BindingResult result
    ) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ObjectResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(ValidationHelper.extractDetailedErrorMessages(result))
                            .build()
            );
        }
        var createdUser = userService.createUser(registerDTO);
        return ResponseEntity.ok(
                ObjectResponse.<UserResponse>builder()
                        .message("User created successfully")
                        .status(HttpStatus.OK)
                        .data(createdUser)
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ObjectResponse<?>> loginUser(
        @RequestBody LoginRequest loginDTO,
        BindingResult result,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ObjectResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(ValidationHelper.extractDetailedErrorMessages(result))
                            .build()
            );
        }
        var token = userService.loginUser(loginDTO);

        boolean secure = request.isSecure(); // hoặc đọc từ profile
        CookieUtil.addCookie(response, "access_token", token, true, secure);

        return ResponseEntity.ok(
                ObjectResponse.<String>builder()
                        .message("User logged in successfully")
                        .status(HttpStatus.OK)
                        .data(token)
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ObjectResponse<Void>> logout(HttpServletResponse response) {
        CookieUtil.deleteCookie(response, "access_token");
        return ResponseEntity.ok(
                ObjectResponse.<Void>builder()
                        .message("User logged out successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse<UserResponse>> getCurrentUser(
        @AuthenticationPrincipal User user
    ) throws Exception {
        var userResponse = userService.getCurrentUser(user);
        return ResponseEntity.ok(
                ObjectResponse.<UserResponse>builder()
                        .message("Current user retrieved successfully")
                        .status(HttpStatus.OK)
                        .data(userResponse)
                        .build()
        );
    }
}
