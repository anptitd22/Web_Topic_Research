package com.example.researcharticles.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.researcharticles.dto.request.AdminRequest;
import com.example.researcharticles.dto.request.LoginRequest;
import com.example.researcharticles.dto.response.AdminResponse;
import com.example.researcharticles.dto.response.ObjectResponse;
import com.example.researcharticles.helper.ValidationHelper;
import com.example.researcharticles.model.Admin;
import com.example.researcharticles.service.AdminServiceImpl;
import com.example.researcharticles.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminServiceImpl adminService;

    @PostMapping("/login")
    public ResponseEntity<ObjectResponse<?>> loginAdmin(
        @RequestBody LoginRequest loginRequest,
        BindingResult result,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ObjectResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(ValidationHelper.extractDetailedErrorMessages(result))
                            .build()
            );
        }
        var token = adminService.loginAdmin(loginRequest);

        boolean secure = request.isSecure(); // hoặc đọc từ profile
        CookieUtil.addCookie(response, "access_token", token, true, secure);

        return ResponseEntity.ok(
                ObjectResponse.<String>builder()
                        .message("Admin logged in successfully")
                        .status(HttpStatus.OK)
                        .data(token)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<AdminResponse> createAdmin(@RequestBody AdminRequest adminDTO) {
        AdminResponse createdAdmin = adminService.createAdmin(adminDTO);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<ObjectResponse<AdminResponse>> currentAdmin(
        @AuthenticationPrincipal Admin admin
    ) {
        var adminResponse = adminService.getCurrentAdmin(admin);
        return ResponseEntity.ok(
                ObjectResponse.<AdminResponse>builder()
                        .message("Admin is ok")
                        .status(HttpStatus.OK)
                        .data(adminResponse)
                        .build()
        );
    }

}
