package com.example.researcharticles.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.researcharticles.model.Role;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String>{
    Optional<Role> findByName(String name);
}
