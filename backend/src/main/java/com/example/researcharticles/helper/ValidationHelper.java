package com.example.researcharticles.helper;

import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public class ValidationHelper {
    public static String extractDetailedErrorMessages(BindingResult result) {
        return result.getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
    }
}
