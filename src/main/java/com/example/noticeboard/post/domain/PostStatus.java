package com.example.noticeboard.post.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PostStatus {
    NEW("새글"), EDIT("수정된글");
    private final String text;
}
