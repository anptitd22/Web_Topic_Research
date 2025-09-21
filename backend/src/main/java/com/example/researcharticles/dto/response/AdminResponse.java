package com.example.researcharticles.dto.response;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AdminResponse {
    private String account;

    private String password;
}
