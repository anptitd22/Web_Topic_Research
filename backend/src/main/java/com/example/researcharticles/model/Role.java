package com.example.researcharticles.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.researcharticles.constant.RoleName;

@Document("roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id private String id;
    
    private RoleName name;
}
