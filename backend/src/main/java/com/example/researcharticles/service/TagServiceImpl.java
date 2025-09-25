package com.example.researcharticles.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.researcharticles.dto.request.TagRequest;
import com.example.researcharticles.dto.response.TagResponse;
import com.example.researcharticles.helper.CheckHelper;
import com.example.researcharticles.mapper.TagMapper;
import com.example.researcharticles.model.Tag;
import com.example.researcharticles.repository.TagRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper mapper;

    @Override
    public TagResponse createTag(TagRequest tagRequest) {
        Tag tag = mapper.toTag(tagRequest);
        return mapper.toTagResponse(tagRepository.save(tag));
    }

    @Override
    public TagResponse updateTag(String id, TagRequest tagRequest) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
        
        mergerTag(existingTag, tagRequest);
        
        return mapper.toTagResponse(tagRepository.save(existingTag));
    }

    public void mergerTag(Tag existingTag, TagRequest tagRequest) {
        existingTag.setName(CheckHelper.checkNullAndUpdate(tagRequest.getName(), existingTag.getName()));
    }

    @Override
    public void deleteTag(String id) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + id));
        
        tagRepository.delete(existingTag);
    }

    @Override
    public TagResponse getTagById(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTagById'");
    }

    @Override
    public List<TagResponse> findAllTags() {
        var tags = tagRepository.findAll();
        List<TagResponse> tagResponses = tags.stream()
                .map(tag -> mapper.toTagResponse(tag))
                .toList();
        return tagResponses;
    }

    @Override
    public TagResponse findById(String tagId) {
        Tag existingTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId));
        return mapper.toTagResponse(existingTag);
    }
}
