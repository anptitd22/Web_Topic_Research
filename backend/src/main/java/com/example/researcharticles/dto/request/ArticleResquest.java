package com.example.researcharticles.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ArticleResquest {
    @NotBlank(message = "Title can not be blank") @Size(max = 255)
    private String title;

    private String content;

    @NotBlank(message = "Author can not be blank") @Size(max = 255)
    private String author;

    @NotBlank(message = "Article key is required")
    @JsonProperty("fileKey")
    private String articleKey; 

    @NotBlank(message = "Article URL is required")
    @JsonProperty("fileUrl")
    private String articleUrl;
}
