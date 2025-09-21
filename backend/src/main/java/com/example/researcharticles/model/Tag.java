package com.example.researcharticles.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Size;
import lombok.*;

@Document("tags")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tag {
    @Id private String id;  

    @Indexed(unique = true)
    @Size(min = 2, max = 255)
    private String name;  
}
