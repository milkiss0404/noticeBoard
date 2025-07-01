package com.example.noticeboard.comment.application.dto;

import com.example.noticeboard.comment.repository.entity.CommentEntity;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.application.dtos.response.ResponsePostSelect;
import com.example.noticeboard.user.application.dtos.response.ResponseUserDto;

import java.time.LocalDateTime;

public record ResponseComment(ResponseUserDto user ,
                              Long commentId,
                              Long postId,
                              String commentContent,
                              LocalDateTime createDateTime,
                              LocalDateTime modifiedDateTime) {

    public static ResponseComment from(CommentEntity comment) {
        return new ResponseComment(
                ResponseUserDto.from(comment.getUser()),
                comment.getId(),
                comment.getPost().getId(),
                comment.getContent(),
                comment.getCreateDateTime(),
                comment.getModifiedDateTime());

    }
}
