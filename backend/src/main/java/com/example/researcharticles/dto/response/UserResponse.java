package com.example.researcharticles.dto.response;

import com.example.researcharticles.model.Role;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    private String id;

    private String account;

    private String password;

    private String email;

    private String phone;

    String avatarKey;

    String avatarUrl;

    private Role role;

    private Boolean isActive;
}
