package com.example.researcharticles.service;

import com.example.researcharticles.dto.request.LoginResquest;
import com.example.researcharticles.dto.request.RegisterResquest;
import com.example.researcharticles.dto.response.UserResponse;
import com.example.researcharticles.model.User;


public interface UserService {
    public UserResponse createUser(RegisterResquest registerDTO) throws Exception;
    public String loginUser(LoginResquest loginDTO) throws Exception;
    public UserResponse getCurrentUser(User user) throws Exception;
    // public UserResponse updateUser(String id, UserResquest userDTO);
    // public void deleteUser(String id);
    // public List<UserResponse> findAllUsers();
}
