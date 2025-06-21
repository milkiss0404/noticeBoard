package com.example.noticeboard.user.application.dtos.response;

import com.example.noticeboard.user.repository.entity.UserEntity;

import java.time.LocalDateTime;

public record ResponseUserDto(Long userId,
                              String username,
                              LocalDateTime createDateTime,
                              LocalDateTime modifiedDateTime) {
    public static ResponseUserDto from(UserEntity user) {
        return new ResponseUserDto(
                user.getId(),
                user.getUsername(),
                user.getCreateDateTime(),
                user.getModifiedDateTime()
        );
    }
}
