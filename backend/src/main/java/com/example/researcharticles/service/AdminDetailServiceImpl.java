package com.example.researcharticles.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.researcharticles.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service("adminDetailsService")
@RequiredArgsConstructor
public class AdminDetailServiceImpl implements UserDetailsService{

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findByAccount(username)
                .orElseThrow(() -> new UsernameNotFoundException("admin not found"));
    }
}
