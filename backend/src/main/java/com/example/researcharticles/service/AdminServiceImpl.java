package com.example.researcharticles.service;

import org.springframework.stereotype.Service;

import com.example.researcharticles.dto.request.AdminResquest;
import com.example.researcharticles.dto.response.AdminResponse;

@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public AdminResponse saveAdmin(AdminResquest adminDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAdmin'");
    }

    @Override
    public AdminResponse updateAdmin(String id, AdminResquest adminDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAdmin'");
    }

    @Override
    public void deleteAdmin(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAdmin'");
    }
    
}
