package com.example.noticeboard.post.repository.entity;

import com.example.noticeboard.post.domain.PostStatus;
import com.example.noticeboard.post.repository.JpaPostRepository;
import com.example.noticeboard.user.repository.JpaUserRepository;
import com.example.noticeboard.user.repository.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.noticeboard.post.domain.PostStatus.NEW;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostEntityTest {
    @Autowired
    private JpaPostRepository jpaPostRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;
    @DisplayName("게시글 생성")
    @Test
    void PostEntityUnitTest() {
        UserEntity user = UserEntity.builder()
                .username("우철")
                .passwd("1234dsa!!")
                .build();
        jpaUserRepository.save(user);


    PostEntity post = PostEntity.builder()
            .id(null)
            .postStatus(NEW)
            .content("안녕하세요")
            .user(user)
            .build();

        jpaPostRepository.save(post);

        Assertions.assertThat(post)
                .extracting("id", "postStatus")
                .containsExactlyInAnyOrder(1L, NEW);
    }

}