package com.example.researcharticles.service;

import java.util.List;

import com.example.researcharticles.dto.request.ArticleRequest;
import com.example.researcharticles.dto.response.ArticleResponse;
import com.example.researcharticles.model.User;

public interface ArticleService {
    public ArticleResponse createArticle(ArticleRequest articleDTO, User user);
    public ArticleResponse updateArticle(String id, ArticleRequest articleDetails, User user) throws Exception;
    public ArticleResponse findArticleById(String articleId);
    public List<ArticleResponse> findMyArticles(User user);
    public void deleteArticle(String id);
    public List<ArticleResponse> findAllArticles();
}
