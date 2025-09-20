package com.example.researcharticles.configurations;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.researcharticles.constant.RoleName;
import com.example.researcharticles.model.Role;
import com.example.researcharticles.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
class DataInitializer {
    private final RoleRepository roleRepository;

    @Bean CommandLineRunner seedRoles() {
        return args -> {
            if (roleRepository.findByName(RoleName.ADMIN.toString()).isEmpty())
                roleRepository.save(Role.builder().name(RoleName.ADMIN).build());
            if (roleRepository.findByName(RoleName.USER.toString()).isEmpty())
                roleRepository.save(Role.builder().name(RoleName.USER).build());
        };
    }
}