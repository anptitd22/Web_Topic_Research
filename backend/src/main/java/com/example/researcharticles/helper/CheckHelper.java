package com.example.researcharticles.helper;

public class CheckHelper<T> {
    public static <T> T checkNullAndUpdate(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
