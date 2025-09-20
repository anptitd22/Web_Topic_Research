package com.example.researcharticles.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;

@Document("tags")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tag {
    @Id private String id;

    @Indexed(unique = true)
    private String slug;   

    @Indexed(unique = true)
    private String name;  
}
