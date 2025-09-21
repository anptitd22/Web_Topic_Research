package com.example.researcharticles.service;

import com.example.researcharticles.dto.request.AdminRequest;
import com.example.researcharticles.dto.request.LoginRequest;
import com.example.researcharticles.dto.response.AdminResponse;
import com.example.researcharticles.model.Admin;

public interface AdminService {
    public AdminResponse createAdmin(AdminRequest adminRequest);
    public AdminResponse updateAdmin(String id, AdminRequest adminDTO);
    public String loginAdmin(LoginRequest loginRequest) throws Exception;
    public AdminResponse getCurrentAdmin(Admin admin);
    public void deleteAdmin(String id);
}
