package com.example.noticeboard.post.application.dtos;

import org.springframework.data.domain.Page;

import java.util.List;

public record ResponsePage<T>(
                           List<T> content,
                           int page,
                           int size,
                           int totalPages,
                           long totalElements,
                           boolean last) {
    public static <T> ResponsePage<T> from(Page<T> page) {
        return new ResponsePage<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }
}
