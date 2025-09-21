package com.example.researcharticles.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.researcharticles.dto.response.ObjectResponse;
import com.example.researcharticles.dto.response.UserResponse;
import com.example.researcharticles.model.User;
import com.example.researcharticles.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse<UserResponse>> getCurrentUser(
        @AuthenticationPrincipal User user
    ) throws Exception {
        var userResponse = userService.getCurrentUser(user);
        return ResponseEntity.ok(
                ObjectResponse.<UserResponse>builder()
                        .message("Current user retrieved successfully")
                        .status(HttpStatus.OK)
                        .data(userResponse)
                        .build()
        );
    }
}
