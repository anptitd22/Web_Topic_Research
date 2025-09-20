package com.example.researcharticles.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TagRepository extends MongoRepository<String, String> {
    
}
