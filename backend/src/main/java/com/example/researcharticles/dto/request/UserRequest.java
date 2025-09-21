package com.example.researcharticles.dto.request;

import org.springframework.data.mongodb.core.mapping.Field;

import com.example.researcharticles.constant.RoleName;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserRequest {

    private String account;

    private String password;

    private String email;

    private String phone;

    private Boolean isActive;

    private RoleName role;

    @Field("avatar_key")
    private String avatarKey; 

    @Field("avatar_url")
    private String avatarUrl;
}
