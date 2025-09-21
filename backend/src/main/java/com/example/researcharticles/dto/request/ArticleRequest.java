package com.example.researcharticles.dto.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ArticleRequest {
    @NotBlank(message = "Title can not be blank") @Size(max = 255)
    private String title;

    private String content;

    @NotBlank(message = "Author can not be blank") @Size(max = 255)
    private String author;

    @NotBlank(message = "Article key is required")
    private String articleKey; 

    @NotBlank(message = "Article URL is required")
    private String articleUrl;

    @NotEmpty(message = "TagIds is required")
    private List<@NotBlank String> tagIds = new ArrayList<>();
}
