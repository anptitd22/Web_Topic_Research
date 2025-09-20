package com.example.researcharticles.configurations;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.example.researcharticles.provider.AdminAuthenticationProvider;
import com.example.researcharticles.provider.UserAuthenticationProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final AdminAuthenticationProvider adminAuthenticationProvider;

    //tai khoan
    @Bean
    public AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = Arrays.asList(
                adminAuthenticationProvider,
                userAuthenticationProvider
        );
        return new ProviderManager(providers);
    }
}