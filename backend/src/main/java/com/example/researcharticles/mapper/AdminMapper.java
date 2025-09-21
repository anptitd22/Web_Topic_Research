package com.example.researcharticles.mapper;

import org.springframework.stereotype.Service;

import com.example.researcharticles.dto.response.AdminResponse;
import com.example.researcharticles.model.Admin;

@Service
public class AdminMapper {
    public AdminResponse toAdminResponse(Admin admin) {
        if (admin == null){
            return null;
        }
        return AdminResponse.builder()
                .account(admin.getAccount())
                .password(admin.getPassword())
                .build();
    }
}
