package com.example.noticeboard.user.application.dtos.response;

import com.example.noticeboard.post.repository.entity.PostEntity;

public record ResponsePostEdit(String title, String userName, String content) {
    public static ResponsePostEdit from(PostEntity postEntity) {
        return new ResponsePostEdit(postEntity.getTitle(),
                ResponseUserDto.from(postEntity.getUser()).username(),
                postEntity.getContent()
        );

    }
}
