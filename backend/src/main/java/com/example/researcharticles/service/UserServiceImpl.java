package com.example.researcharticles.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.researcharticles.constant.RoleName;
import com.example.researcharticles.dto.request.LoginResquest;
import com.example.researcharticles.dto.request.RegisterResquest;
import com.example.researcharticles.dto.response.UserResponse;
import com.example.researcharticles.helper.CheckHelper;
import com.example.researcharticles.helper.JwtTokenHelper;
import com.example.researcharticles.mapper.UserMapper;
import com.example.researcharticles.model.User;
import com.example.researcharticles.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(RegisterResquest registerResquest) throws Exception {

        if (!registerResquest.getPassword().equals(registerResquest.getRetypePassword())){
            throw new Exception("Password and Retype Password do not match");
        }

        if (registerResquest.getRole() == null || !checkRole(registerResquest.getRole())) {
            throw new Exception("Role is not valid");
        }

        if (userRepository.findByAccount(registerResquest.getAccount()).isPresent()) {
            throw new Exception("User already exists with account " + registerResquest.getAccount());
        }
        
        var user = createNewUser(registerResquest);

        var savedUser = userRepository.save(user);

        return mapper.toUserResponse(savedUser);
    }

    public User createNewUser(RegisterResquest registerResquest) {
        return User.builder()
                .account(registerResquest.getAccount())
                .password(passwordEncoder.encode(registerResquest.getPassword()))
                .role(registerResquest.getRole())
                .isActive(true)
                .build();
    }

    @Override
    public String loginUser(LoginResquest loginResquest) throws Exception {

        if(!checkRole(loginResquest.getRole())){
            throw new Exception("Role is not valid");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginResquest.getAccount(),
                        loginResquest.getPassword()
                )
        );

        User existingUser = (User) authentication.getPrincipal();

        return jwtTokenHelper.generateTokenFromUser(existingUser);
    }

    public Boolean checkRole(RoleName roleName) throws Exception {
        return roleName.equals(RoleName.USER);
    }

    public User mergerdUser(User existingUser, User newUser) {
        CheckHelper.checkNullAndUpdate(newUser.getAccount(), existingUser.getAccount());
        CheckHelper.checkNullAndUpdate(newUser.getPassword(), existingUser.getPassword());
        CheckHelper.checkNullAndUpdate(newUser.getEmail(), existingUser.getEmail());
        CheckHelper.checkNullAndUpdate(newUser.getPhone(), existingUser.getPhone());
        CheckHelper.checkNullAndUpdate(newUser.getRole(), existingUser.getRole());
        CheckHelper.checkNullAndUpdate(newUser.getIsActive(), existingUser.getIsActive());
        CheckHelper.checkNullAndUpdate(newUser.getAvatarKey(), existingUser.getAvatarKey());
        CheckHelper.checkNullAndUpdate(newUser.getAvatarUrl(), existingUser.getAvatarUrl());
        return existingUser;
    }

    @Override
    public UserResponse getCurrentUser(User user) throws Exception {
        return mapper.toUserResponse(user);
    }
}
