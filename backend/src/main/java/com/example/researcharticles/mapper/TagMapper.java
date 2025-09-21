package com.example.researcharticles.mapper;

import org.springframework.stereotype.Service;

import com.example.researcharticles.dto.request.TagRequest;
import com.example.researcharticles.dto.response.TagResponse;
import com.example.researcharticles.model.Tag;

@Service
public class TagMapper {
    public TagResponse toTagResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return TagResponse.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public Tag toTag(TagRequest tagRequest) {
        if (tagRequest == null) {
            return null;
        }
        return Tag.builder()
                .name(tagRequest.getName())
                .build();
    }
}
