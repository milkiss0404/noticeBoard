package com.example.noticeboard.post.domain;

import com.example.noticeboard.user.repository.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Post {

    private Long id;
    private String title;
    private String content;
    private UserEntity user;
    private PostStatus postStatus;

}
