package com.example.researcharticles.configurations;

import io.minio.MinioClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Bean
    MinioClient minio(
        @Value("${minio.url}") String url,
        @Value("${minio.accessKey}") String ak,
        @Value("${minio.secretKey}") String sk) {
        return MinioClient.builder().endpoint(url).credentials(ak, sk).build();
    }
}
