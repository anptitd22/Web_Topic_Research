package com.example.researcharticles.service;

import com.example.researcharticles.dto.request.AdminResquest;
import com.example.researcharticles.dto.response.AdminResponse;

public interface AdminService {
    public AdminResponse saveAdmin(AdminResquest adminDTO);
    public AdminResponse updateAdmin(String id, AdminResquest adminDTO);
    public void deleteAdmin(String id);
}
