package com.example.researcharticles.dto.response;

import java.time.Instant;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ArticleResponse {
    private String id;

    private String title;

    private String content;

    private String author;

    private String articleKey;

    private String articleUrl;

    private String UserId;

    private String UserName;

    private Instant createdAt;

    private Instant updatedAt;
}
