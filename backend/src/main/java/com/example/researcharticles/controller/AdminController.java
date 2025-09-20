package com.example.researcharticles.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.researcharticles.dto.request.AdminResquest;
import com.example.researcharticles.dto.response.AdminResponse;
import com.example.researcharticles.service.AdminServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminServiceImpl adminService;

    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody AdminResquest adminDTO) {
        AdminResponse createdAdmin = adminService.saveAdmin(adminDTO);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

}
