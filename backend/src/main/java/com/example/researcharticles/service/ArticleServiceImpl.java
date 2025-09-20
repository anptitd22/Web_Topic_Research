package com.example.researcharticles.service;
import com.example.researcharticles.dto.request.ArticleResquest;
import com.example.researcharticles.dto.response.ArticleResponse;
import com.example.researcharticles.helper.CheckHelper;
import com.example.researcharticles.mapper.ArticleMapper;
import com.example.researcharticles.model.Article;
import com.example.researcharticles.model.User;
import com.example.researcharticles.repository.ArticleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper mapper;

    public ArticleResponse createArticle(ArticleResquest articleResquest, User user) { 

        var article = mapper.toArticle(articleResquest, user.getId(), user.getUsername());

        var articleSave = articleRepository.save(article);
        return mapper.toArticleResponse(articleSave);
    }

    public ArticleResponse updateArticle(String id, ArticleResquest articleResquest, User user) throws Exception {

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));

        mergerdArticle(article, articleResquest, user);

        var aricleUpdate = articleRepository.save(article);
        return mapper.toArticleResponse(aricleUpdate);
    }

    private void mergerdArticle(Article article, ArticleResquest articleResquest, User user) {
        article.setTitle(CheckHelper.checkNullAndUpdate(articleResquest.getTitle(), article.getTitle()));
        article.setContent(CheckHelper.checkNullAndUpdate(articleResquest.getContent(), article.getContent()));
        article.setAuthor(CheckHelper.checkNullAndUpdate(articleResquest.getAuthor(), article.getAuthor()));
        article.setArticleKey(CheckHelper.checkNullAndUpdate(articleResquest.getArticleKey(), article.getArticleKey()));
        article.setArticleUrl(CheckHelper.checkNullAndUpdate(articleResquest.getArticleUrl(), article.getArticleUrl()));
        article.setUserId(CheckHelper.checkNullAndUpdate(user.getId(), article.getUserId()));
        article.setUserName(CheckHelper.checkNullAndUpdate(user.getUsername(), article.getUserName()));
    }

    public void deleteArticle(String id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + id));
        articleRepository.delete(article);
    }

    public List<ArticleResponse> findAllArticles() {
        var articles = articleRepository.findAll();
        List<ArticleResponse> articleResponses = articles.stream()
                .map(article -> mapper.toArticleResponse(article))
                .toList();
        return articleResponses;
    }

    public ArticleResponse findArticleById(String articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found with id " + articleId));
        return mapper.toArticleResponse(article);
    }

    public List<ArticleResponse> searchAuthorOrTitle(String key) {
        var list = articleRepository.searchByTitleOrAuthorRegex(key);
        return list.stream().map(mapper::toArticleResponse).toList();
    }
}