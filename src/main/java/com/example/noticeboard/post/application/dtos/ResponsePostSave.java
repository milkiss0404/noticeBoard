package com.example.noticeboard.post.application.dtos;

import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.application.dtos.response.ResponseUserDto;

public record ResponsePostSave(Long id,
                               String content,
                               String postStatus,
                               ResponseUserDto user) {

    public static ResponsePostSave from(PostEntity post) {
        return new ResponsePostSave(
                post.getId(),
                post.getContent(),
                post.getPostStatus().getText(),
                ResponseUserDto.from(post.getUser())
        );
    }
}
