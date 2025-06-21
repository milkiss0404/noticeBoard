package com.example.noticeboard.comment.application.service;

import com.example.noticeboard.comment.application.RequestCommentSave;
import com.example.noticeboard.comment.repository.JpaCommentRepository;
import com.example.noticeboard.comment.repository.entity.CommentEntity;
import com.example.noticeboard.post.application.dtos.RequestPostSave;
import com.example.noticeboard.post.application.service.PostService;
import com.example.noticeboard.post.repository.JpaPostRepository;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.repository.JpaUserRepository;
import com.example.noticeboard.user.repository.entity.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {
    @Autowired
    private JpaUserRepository jpaUserRepository;
    @Autowired
    private JpaPostRepository jpaPostRepository;
    @Autowired
    private CommentService commentService;

    @DisplayName("댓글 저장후 DB 안에 있는지 확인")
    @Test
    void createComment() {
        UserEntity user =
                jpaUserRepository.save(new UserEntity("username", "password"));

        PostEntity post =
                jpaPostRepository.save(new PostEntity("passwd","글내용",user));

        RequestCommentSave request = new RequestCommentSave(user.getId(), post.getId(), "댓글");


        Long commentId = commentService.createComment(request);

        assertThat(commentId).isNotNull();

    }
}