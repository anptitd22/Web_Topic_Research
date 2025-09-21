package com.example.researcharticles.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.researcharticles.dto.request.TagRequest;
import com.example.researcharticles.dto.response.ObjectResponse;
import com.example.researcharticles.dto.response.TagResponse;
import com.example.researcharticles.helper.ValidationHelper;
import com.example.researcharticles.service.TagService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${api.prefix}/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<?> createTag(
        @RequestBody @Valid TagRequest tagRequest, 
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ObjectResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(ValidationHelper.extractDetailedErrorMessages(result))
                            .build()
            );
        }
        var createdTag = tagService.createTag(tagRequest);
        return ResponseEntity.ok(
                ObjectResponse.<TagResponse>builder()
                        .message("Tag created successfully")
                        .status(HttpStatus.OK)
                        .data(createdTag)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ObjectResponse<List<TagResponse>>> listTags(
    ) {
        var list = tagService.findAllTags();
        return ResponseEntity.ok(
            ObjectResponse.<List<TagResponse>>builder()
            .message("Tags retrieved successfully")
            .status(HttpStatus.OK)
            .data(list)
            .build()
        );
    }

    @PutMapping(params = "tagId")
    public ResponseEntity<?> updateTag(
        @RequestParam("tagId") String tagId,
        @RequestBody @Valid TagRequest tagRequest,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    ObjectResponse.<Void>builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(ValidationHelper.extractDetailedErrorMessages(result))
                            .build()
            );
        }
        var updateTag = tagService.updateTag(tagId, tagRequest);
        return ResponseEntity.ok(
                ObjectResponse.<TagResponse>builder()
                        .message("Tag created successfully")
                        .status(HttpStatus.OK)
                        .data(updateTag)
                        .build()
        );
    }

    @DeleteMapping(params = "tagId")
    public ResponseEntity<ObjectResponse<Void>> deleteTag(
        @RequestParam("tagId") String tagId
    ) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok(
                ObjectResponse.<Void>builder()
                        .message("Tag deleted successfully")
                        .status(HttpStatus.OK)
                        .build()
        );
    }
}
