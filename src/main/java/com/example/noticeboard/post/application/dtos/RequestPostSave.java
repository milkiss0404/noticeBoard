package com.example.noticeboard.post.application.dtos;

public record RequestPostSave(Long userId, String postPasswd,String title,String content) {
}
