package com.example.researcharticles.service;

import java.util.List;

import com.example.researcharticles.dto.request.ArticleResquest;
import com.example.researcharticles.dto.response.ArticleResponse;

public interface ArticleService {
    public ArticleResponse createArticle(ArticleResquest articleDTO) throws Exception;
    public ArticleResponse updateArticle(String id, ArticleResquest articleDetails);
    public ArticleResponse findArticleById(String articleId);
    public void deleteArticle(String id);
    public List<ArticleResponse> findAllArticles();
}
