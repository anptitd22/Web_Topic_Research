package com.example.researcharticles.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.researcharticles.constant.RoleName;
import com.example.researcharticles.dto.request.AdminRequest;
import com.example.researcharticles.dto.request.LoginRequest;
import com.example.researcharticles.dto.response.AdminResponse;
import com.example.researcharticles.helper.JwtTokenHelper;
import com.example.researcharticles.mapper.AdminMapper;
import com.example.researcharticles.model.Admin;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    // private final AdminRepository adminRepository;
    private final AuthenticationManager authenticationManager;
    // private final PasswordEncoder passwordEncoder;
    private final JwtTokenHelper jwtTokenHelper;
    private final AdminMapper mapper;

    @Override
    public AdminResponse createAdmin(AdminRequest adminRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAdmin'");
    }

    @Override
    public AdminResponse updateAdmin(String id, AdminRequest adminDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAdmin'");
    }

    @Override
    public void deleteAdmin(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAdmin'");
    }

    @Override
    public String loginAdmin(LoginRequest loginRequest) throws Exception{

        if(!checkRole(loginRequest.getRole())){
            throw new Exception("Role is not valid");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getAccount(),
                        loginRequest.getPassword()
                )
        );

        Admin existingAdmin = (Admin) authentication.getPrincipal();

        return jwtTokenHelper.generateTokenFromAdmin(existingAdmin);
    }

    public Boolean checkRole(RoleName roleName) throws Exception {
        return roleName.equals(RoleName.ADMIN);
    }

    @Override
    public AdminResponse getCurrentAdmin(Admin admin) {
        return mapper.toAdminResponse(admin);
    }
    
}
