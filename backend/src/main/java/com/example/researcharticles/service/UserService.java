package com.example.researcharticles.service;

import com.example.researcharticles.dto.request.LoginRequest;
import com.example.researcharticles.dto.request.RegisterRequest;
import com.example.researcharticles.dto.response.UserResponse;
import com.example.researcharticles.model.User;


public interface UserService {
    public UserResponse createUser(RegisterRequest registerDTO) throws Exception;
    public String loginUser(LoginRequest loginDTO) throws Exception;
    public UserResponse getCurrentUser(User user) throws Exception;
    // public UserResponse updateUser(String id, UserResquest userDTO);
    // public void deleteUser(String id);
    // public List<UserResponse> findAllUsers();
}
