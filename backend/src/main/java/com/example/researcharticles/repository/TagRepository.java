package com.example.researcharticles.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.researcharticles.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {
    
}
