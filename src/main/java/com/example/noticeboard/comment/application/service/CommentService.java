package com.example.noticeboard.comment.application.service;

import com.example.noticeboard.comment.application.RequestCommentSave;
import com.example.noticeboard.comment.repository.JpaCommentRepository;
import com.example.noticeboard.comment.repository.entity.CommentEntity;
import com.example.noticeboard.post.repository.JpaPostRepository;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.repository.JpaUserRepository;
import com.example.noticeboard.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final JpaUserRepository jpauserRepository;
    private final JpaPostRepository jpaPostRepository;
    private final JpaCommentRepository jpaCommentRepository;

    public Long createComment(RequestCommentSave dto) {
        UserEntity user = getUserEntity(dto);

        PostEntity post = getPostEntity(dto);

        CommentEntity comment = CommentEntity.builder()
                .content(dto.content())
                .post(post)
                .user(user)
                .build();
        jpaCommentRepository.save(comment);
        return comment.getId();
    }

    private PostEntity getPostEntity(RequestCommentSave dto) {
        return jpaPostRepository.findById(dto.postId())
                .orElseThrow(() -> new IllegalArgumentException("없는 게시글 입니다."));
    }

    private UserEntity getUserEntity(RequestCommentSave dto) {
        return jpauserRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 없습니다"));
    }

}
