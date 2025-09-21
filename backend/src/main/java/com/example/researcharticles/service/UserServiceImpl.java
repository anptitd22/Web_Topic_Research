package com.example.researcharticles.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.researcharticles.constant.RoleName;
import com.example.researcharticles.dto.request.LoginRequest;
import com.example.researcharticles.dto.request.RegisterRequest;
import com.example.researcharticles.dto.request.UserRequest;
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
    public UserResponse createUser(RegisterRequest registerResquest) throws Exception {

        if (!registerResquest.getPassword().equals(registerResquest.getRetypePassword())){
            throw new Exception("Password and Retype Password do not match");
        }

        checkRole(registerResquest.getRole());

        if (userRepository.findByAccount(registerResquest.getAccount()).isPresent()) {
            throw new Exception("User already exists with account " + registerResquest.getAccount());
        }
        
        var user = createNewUser(registerResquest);

        var savedUser = userRepository.save(user);

        return mapper.toUserResponse(savedUser);
    }

    public User createNewUser(RegisterRequest registerResquest) {
        return User.builder()
                .account(registerResquest.getAccount())
                .password(passwordEncoder.encode(registerResquest.getPassword()))
                .role(registerResquest.getRole())
                .isActive(true)
                .build();
    }

    @Override
    public String loginUser(LoginRequest loginRequest) throws Exception {

        checkRole(loginRequest.getRole());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getAccount(),
                        loginRequest.getPassword()
                )
        );

        User existingUser = (User) authentication.getPrincipal();

        return jwtTokenHelper.generateTokenFromUser(existingUser);
    }

    public void checkRole(RoleName roleName) throws Exception {
        if (!roleName.equals(RoleName.USER)) {
            throw new Exception("Role is not valid");
        }
    }

    public void mergerdUser(User existingUser, UserRequest  userRequest) {
        existingUser.setAccount(CheckHelper.checkNullAndUpdate(userRequest.getAccount(), existingUser.getAccount()));
        existingUser.setPassword(CheckHelper.checkNullAndUpdate(passwordEncoder.encode(userRequest.getPassword()), existingUser.getPassword()));
        existingUser.setEmail(CheckHelper.checkNullAndUpdate(userRequest.getEmail(), existingUser.getEmail()));
        existingUser.setPhone(CheckHelper.checkNullAndUpdate(userRequest.getPhone(), existingUser.getPhone()));
        existingUser.setIsActive(CheckHelper.checkNullAndUpdate(userRequest.getIsActive(), existingUser.getIsActive()));
        existingUser.setAvatarKey(CheckHelper.checkNullAndUpdate(userRequest.getAvatarKey(), existingUser.getAvatarKey()));
        existingUser.setAvatarUrl(CheckHelper.checkNullAndUpdate(userRequest.getAvatarUrl(), existingUser.getAvatarUrl()));
    }

    @Override
    public UserResponse getCurrentUser(User user) throws Exception {
        return mapper.toUserResponse(user);
    }
}
