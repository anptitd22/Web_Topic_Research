package com.example.researcharticles.dto.request;

import com.example.researcharticles.constant.RoleName;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RegisterResquest {
    @NotBlank(message = "Account can not be blank") @Size(max = 100, min = 5)
    private String account;

    @NotBlank(message = "Password can not be blank") @Size(max = 255, min = 10)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(message = "Password can not be blank") @Size(max = 255, min = 10)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String retypePassword;

    @NotBlank(message = "Role can not be blank")
    private RoleName role;
}
