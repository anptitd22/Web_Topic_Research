package com.example.researcharticles.service;

import java.util.List;

import com.example.researcharticles.dto.request.TagRequest;
import com.example.researcharticles.dto.response.TagResponse;

public interface TagService {
    public TagResponse createTag(TagRequest tagRequest); 
    public TagResponse updateTag(String id, TagRequest tagRequest);
    public void deleteTag(String id);
    public TagResponse getTagById(String id);
    public List<TagResponse> findAllTags();
    public TagResponse findById(String tagId);
}
