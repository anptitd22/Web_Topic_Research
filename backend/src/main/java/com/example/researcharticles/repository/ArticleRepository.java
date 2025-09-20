package com.example.researcharticles.repository;
import com.example.researcharticles.model.Article;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<Article, String> {
}