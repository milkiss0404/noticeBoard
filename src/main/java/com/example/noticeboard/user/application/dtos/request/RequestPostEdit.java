package com.example.noticeboard.user.application.dtos.request;

public record RequestPostEdit(Long userId, Long postId, String passwd,String title,String content) {
}
