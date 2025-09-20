package com.example.researcharticles.mapper;

import org.springframework.stereotype.Service;

import com.example.researcharticles.dto.response.UserResponse;
import com.example.researcharticles.model.User;

@Service
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword())
                .phone(user.getPhone())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .email(user.getEmail())
                .avatarKey(user.getAvatarKey())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}
