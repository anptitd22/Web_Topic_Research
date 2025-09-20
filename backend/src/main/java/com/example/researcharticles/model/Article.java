package com.example.researcharticles.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Document(collection = "articles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    @Id
    private String id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    private String content;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Title must be at most 100 characters")
    private String author;

    @Field("article_key")
    @NotBlank(message = "Article key is required")
    private String articleKey; 

    @Field("article_url")
    @NotBlank(message = "Article URL is required")
    private String articleUrl;
}