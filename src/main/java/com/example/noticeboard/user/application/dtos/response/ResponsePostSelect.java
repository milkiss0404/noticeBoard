package com.example.noticeboard.user.application.dtos.response;


import com.example.noticeboard.comment.application.dto.ResponseComment;
import com.example.noticeboard.post.repository.entity.PostEntity;

import java.time.LocalDateTime;
import java.util.List;

public record ResponsePostSelect(Long postId,
                                 ResponseUserDto user,
                                 String title,
                                 String content,
                                 String postStatus,
                                 LocalDateTime createDateTime,
                                 LocalDateTime modifiedDateTime,
                                 List<ResponseComment> comments) {
    public static ResponsePostSelect from(PostEntity post) {
        return new ResponsePostSelect(
                post.getId(),
                ResponseUserDto.from(post.getUser()),
                post.getTitle(),
                post.getContent(),
                post.getPostStatus().getText(),
                post.getCreateDateTime(),
                post.getModifiedDateTime(),
                post.getComments().stream()
                        .map(ResponseComment::from)
                        .toList()
        );
    }

}
