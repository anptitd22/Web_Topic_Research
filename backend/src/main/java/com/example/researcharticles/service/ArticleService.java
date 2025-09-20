package com.example.researcharticles.service;

import java.util.List;

import com.example.researcharticles.dto.request.ArticleResquest;
import com.example.researcharticles.dto.response.ArticleResponse;
import com.example.researcharticles.model.User;

public interface ArticleService {
    public ArticleResponse createArticle(ArticleResquest articleDTO, User user);
    public ArticleResponse updateArticle(String id, ArticleResquest articleDetails, User user) throws Exception;
    public ArticleResponse findArticleById(String articleId);
    public void deleteArticle(String id);
    public List<ArticleResponse> findAllArticles();
}
