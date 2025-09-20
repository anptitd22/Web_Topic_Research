package com.example.researcharticles.dto.response;

import java.time.Instant;

import com.example.researcharticles.constant.RoleName;

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

    private String avatarKey;

    private String avatarUrl;

    private RoleName role;

    private Boolean isActive;

    private Instant createdAt;

    private Instant updatedAt;
}
