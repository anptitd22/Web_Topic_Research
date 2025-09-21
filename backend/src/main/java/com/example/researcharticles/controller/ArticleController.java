package com.example.researcharticles.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.researcharticles.dto.request.ArticleRequest;
import com.example.researcharticles.dto.response.ArticleResponse;
import com.example.researcharticles.dto.response.ObjectResponse;
import com.example.researcharticles.model.User;
import com.example.researcharticles.service.ArticleServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleServiceImpl articleService;

    @GetMapping
    public ResponseEntity<ObjectResponse<List<ArticleResponse>>> getAllArticles() throws Exception {
        List<ArticleResponse> articles = articleService.findAllArticles();
        return ResponseEntity.ok(
                ObjectResponse.<List<ArticleResponse>>builder()
                        .message("Articles retrieved successfully")
                        .status(HttpStatus.OK)
                        .data(articles)
                        .build()
        );
    }
    @GetMapping(params = "articleId")
    public ResponseEntity<ObjectResponse<ArticleResponse>> getArticleById(
        @RequestParam("articleId") String articleId
        ) throws Exception {
        ArticleResponse article = articleService.findArticleById(articleId);
        return ResponseEntity.ok(
                ObjectResponse.<ArticleResponse>builder()
                        .message("Article retrieved successfully")
                        .status(HttpStatus.OK)
                        .data(article)
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse<ArticleResponse>> createArticle(
        @RequestBody @Valid ArticleRequest articleResquest,
        @AuthenticationPrincipal User user
        ) {
        ArticleResponse createdArticle = articleService.createArticle(articleResquest, user);
        return ResponseEntity.ok(
                ObjectResponse.<ArticleResponse>builder()
                        .message("Article created successfully")
                        .status(HttpStatus.OK)
                        .data(createdArticle)
                        .build()
        );
    }

    @PutMapping(params = "articleId")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse<ArticleResponse>> updateArticle(
        @RequestParam("articleId") String articleId, 
        @RequestBody @Valid ArticleRequest articleResquest,
        @AuthenticationPrincipal User user
        ) throws Exception {
        ArticleResponse updatedArticle = articleService.updateArticle(articleId, articleResquest, user);
        return ResponseEntity.ok(
                ObjectResponse.<ArticleResponse>builder()
                        .message("Article updated successfully")
                        .status(HttpStatus.OK)
                        .data(updatedArticle)
                        .build()
        );
    }

    @DeleteMapping(params = "articleId")
    public ResponseEntity<ObjectResponse<Void>> deleteArticle(
        @RequestParam("articleId") String articleId
        ) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok(
                ObjectResponse.<Void>builder()
                        .message("Article deleted successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }

    @GetMapping(value = "/search", params = "key")
    public ResponseEntity<ObjectResponse<List<ArticleResponse>>> searchAuthorOrTitle(
            @RequestParam("key") String key) {

        var result = articleService.searchAuthorOrTitle(key);

        return ResponseEntity.ok(
            ObjectResponse.<List<ArticleResponse>>builder()
                .message("Search successfully")
                .status(HttpStatus.OK)
                .data(result)
                .build()
        );
    }

    @GetMapping(value = "/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse<List<ArticleResponse>>> myArticles(
        @AuthenticationPrincipal User user
    ) {
        var list = articleService.findMyArticles(user);
        return ResponseEntity.ok(
            ObjectResponse.<List<ArticleResponse>>builder()
                .message("My articles")
                .status(HttpStatus.OK)
                .data(list)
                .build()
        );
    }
}