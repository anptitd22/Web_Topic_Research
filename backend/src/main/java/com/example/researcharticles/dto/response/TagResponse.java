package com.example.researcharticles.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TagResponse {
    private String id;  

    private String name;
}
