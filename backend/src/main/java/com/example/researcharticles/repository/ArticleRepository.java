package com.example.researcharticles.repository;
import com.example.researcharticles.model.Article;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ArticleRepository extends MongoRepository<Article, String> {
    @Query("{ $or: [ { 'title':  { $regex: ?0, $options: 'i' } }, " +
            "         { 'author': { $regex: ?0, $options: 'i' } } ] }")
    List<Article> searchByTitleOrAuthorRegex(String regex);

    List<Article> findByTagIdsIn(Collection<String> tagIds);
}