package com.example.researcharticles.model;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
public class Article extends BaseDocument {
    @Id
    private String id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    @Indexed
    private String title;

    private String content;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Title must be at most 100 characters")
    @Indexed
    private String author;

    @Field("article_key")
    @NotBlank(message = "Article key is required")
    private String articleKey; 

    @Field("article_url")
    @NotBlank(message = "Article URL is required")
    private String articleUrl;

    @Field("user_id")
    @Indexed
    @NotBlank(message = "User ID is required")
    private String userId;      

    @Field("user_name")
    @NotBlank(message = "User Name is required")
    private String userName;

    @Indexed
    @Field("tag_ids")
    @Builder.Default
    @Size(min = 1, max = 5, message = "An article must have between 1 and 5 tags")
    private List<String> tagIds = new ArrayList<>();
}