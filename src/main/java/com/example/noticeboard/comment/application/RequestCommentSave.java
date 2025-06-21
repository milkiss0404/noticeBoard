package com.example.noticeboard.comment.application;

public record RequestCommentSave(Long userId,Long postId ,String content) {
}
