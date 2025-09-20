package com.example.researcharticles.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.researcharticles.dto.response.ObjectResponse;
import com.example.researcharticles.model.User;
import com.example.researcharticles.service.MinIOService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/uploads")
@RequiredArgsConstructor
public class UploadController {
    private final MinIOService minIOService;
    // private final UserServiceImpl userService;
    // private final ArticleServiceImpl articleService;

    @PostMapping(value="/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse<Map<String,Object>>> upload(
        @AuthenticationPrincipal User user,
        @RequestPart("file") MultipartFile file
    ) throws Exception {
        minIOService.delete(user.getAvatarKey());

        String key = minIOService.uploadAvatar(user.getId(), file);
        String url = minIOService.publicUrl(key); // null nếu không public bucket

        Map<String, Object> map = Map.of(
            "fileKey", key,
            "fileUrl", url
        );

        // nếu không public, trả presigned GET cho FE dùng tạm
        if (url == null) url = minIOService.presignedGet(key, 60);
        return ResponseEntity.ok(
                ObjectResponse.<Map<String,Object>>builder()
                        .message("Upload avatar successfully")
                        .status(HttpStatus.OK)
                        .data(map)
                        .build()
        );
    }

    @PostMapping(value="/article", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ObjectResponse<Map<String, Object>>> uploadArticleFile(
        @AuthenticationPrincipal User user,
        @RequestPart("file") MultipartFile file
    ) throws Exception {
        String key = minIOService.uploadArticleFile(user.getId(), file);
        String url = minIOService.publicUrl(key); // null nếu không public bucket

        Map<String, Object> map = Map.of(
            "fileKey", key,
            "fileUrl", url
        );

        // nếu không public, trả presigned GET cho FE dùng tạm
        if (url == null) url = minIOService.presignedGet(key, 60);
        return ResponseEntity.ok(
                ObjectResponse.<Map<String, Object>>builder()
                        .message("Upload article file successfully")
                        .status(HttpStatus.OK)
                        .data(map)
                        .build()
        );
    }

    @GetMapping("/file-url")
    public ResponseEntity<ObjectResponse<Map<String, String>>> getFileUrl(
        @RequestParam("key") String key
    ) throws Exception {
        String url = minIOService.presignedGet(key, 60); // link tạm 60 phút
        return ResponseEntity.ok(
            ObjectResponse.<Map<String,String>>builder()
                .status(HttpStatus.OK)
                .message("OK")
                .data(Map.of("url", url))
                .build()
        );
    }
}
