package com.example.researcharticles.constant;

public enum RoleName {
    ADMIN, USER;

    public static RoleName from(String v) {
        return RoleName.valueOf(v.trim().toUpperCase());
    }
}
