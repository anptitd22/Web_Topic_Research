package com.example.researcharticles.mapper;

import org.springframework.stereotype.Service;

import com.example.researcharticles.dto.request.ArticleResquest;
import com.example.researcharticles.dto.response.ArticleResponse;
import com.example.researcharticles.model.Article;

@Service
public class ArticleMapper {
    public Article toArticle(ArticleResquest resquest, String userId, String userName) {
        if (resquest == null) {
            return null;
        }
        return Article.builder()
                .title(resquest.getTitle())
                .content(resquest.getContent())
                .author(resquest.getAuthor())
                .articleKey(resquest.getArticleKey())
                .articleUrl(resquest.getArticleUrl())
                .UserId(userId)
                .UserName(userName)
                .build();
    }

    public ArticleResponse toArticleResponse(Article article) {
        if (article == null) {
            return null;
        }
        return ArticleResponse.builder()
                .id(article.getId())
                .title(article.getTitle())
                .content(article.getContent())
                .author(article.getAuthor())
                .articleKey(article.getArticleKey())
                .articleUrl(article.getArticleUrl())
                .UserId(article.getUserId())
                .UserName(article.getUserName())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }
}
