package com.example.noticeboard.post.application.service;

import com.example.noticeboard.post.application.dtos.RequestPostSave;
import com.example.noticeboard.post.repository.JpaPostRepository;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.repository.JpaUserRepository;
import com.example.noticeboard.user.repository.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostServiceTest {
    @Autowired
    JpaUserRepository jpaUserRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private JpaPostRepository jpaPostRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("게시글 저장 후 DB에 저장됐는지 까지 확인")
    @Test
    void createPost() {
        //given
        UserEntity user =
                jpaUserRepository.save(new UserEntity("username", "password"));

        RequestPostSave postRequest =
                new RequestPostSave(user.getId(), "password","글내용");

        //when
        postService.savePost(postRequest);
        //then
        List<PostEntity> posts = jpaPostRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getContent()).isEqualTo("글내용");
        assertThat(passwordEncoder.matches("password", posts.get(0).getPasswd())).isTrue();
    }

}