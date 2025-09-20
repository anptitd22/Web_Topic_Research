package com.example.researcharticles.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;

import static com.example.researcharticles.helper.FileHelper.isImageFile;
import static com.example.researcharticles.helper.FileHelper.isValidDocument;
import static com.example.researcharticles.helper.FileHelper.createObjectKey;

@Service
@RequiredArgsConstructor
public class MinIOService {
    // Implementation for MinIO operations 
    private final MinioClient minio;

    @Value("${minio.bucket}") String bucket;
    @Value("${minio.publicBaseUrl:}") String publicBaseUrl;

    public String uploadAvatar(String userId, MultipartFile file) throws Exception {
        // Validate
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!isImageFile(file)) {
            throw new IllegalArgumentException("File is not an image");
        }
        // Object key (đổi tên mỗi lần để bust cache)
        String key = createObjectKey("avatars", userId, file);

        // Đảm bảo bucket tồn tại (chạy 1 lần lúc start cũng được)
        checkBucketExists();

        // Upload
        minio.putObject(PutObjectArgs.builder()
            .bucket(bucket)
            .object(key)
            .stream(file.getInputStream(), file.getSize(), -1)
            .contentType(file.getContentType())
            .build());

        return key;
    }

    public String uploadArticleFile(String articleId, MultipartFile file) throws Exception {
        // Validate
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        
        if (!isValidDocument(file)
        ) {
            throw new IllegalArgumentException("File is not a valid document");
        }

        // Object key (đổi tên mỗi lần để bust cache)
        String key = createObjectKey("articles", articleId, file);

        // Đảm bảo bucket tồn tại (chạy 1 lần lúc start cũng được)
        checkBucketExists();

        // Upload
        minio.putObject(PutObjectArgs.builder()
            .bucket(bucket)
            .object(key)
            .stream(file.getInputStream(), file.getSize(), -1)
            .contentType(file.getContentType())
            .build());

        return key;
    }

    public void checkBucketExists() throws Exception {
        boolean exists = minio.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) minio.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }

    public String publicUrl(String key) {
        if (publicBaseUrl == null || publicBaseUrl.isBlank()) return null;
        return String.format("%s/%s/%s", publicBaseUrl, bucket, key);
    }

    public String presignedGet(String key, int minutes) throws Exception {
        return minio.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
            .bucket(bucket).object(key)
            .method(Method.GET).expiry(minutes)
            .build());
    }

    public void delete(String key) throws Exception {
        if (key != null) {
        minio.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(key).build());
        }
    }


}
