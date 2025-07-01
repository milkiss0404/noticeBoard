package com.example.noticeboard.comment.application.dto;

public record RequestCommentSave(Long userId,Long postId ,String content) {
}
