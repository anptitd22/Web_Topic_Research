package com.example.researcharticles.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.example.researcharticles.dto.response.ObjectResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ObjectResponse<Void>> handGeneralException (
        Exception exception
    ){
        return ResponseEntity.badRequest().body(
                ObjectResponse.<Void>builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ObjectResponse<Void>> handUsernameNotFoundException (
        UsernameNotFoundException exception
    ){
        return ResponseEntity.badRequest().body(
                ObjectResponse.<Void>builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
        );
    }
}
