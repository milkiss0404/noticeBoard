package com.example.noticeboard.comment.application.service;

import com.example.noticeboard.comment.application.dto.RequestCommentRemove;
import com.example.noticeboard.comment.application.dto.RequestCommentSave;
import com.example.noticeboard.comment.application.dto.RequestCommentEdit;
import com.example.noticeboard.comment.repository.JpaCommentRepository;
import com.example.noticeboard.comment.repository.entity.CommentEntity;
import com.example.noticeboard.post.repository.JpaPostRepository;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.repository.JpaUserRepository;
import com.example.noticeboard.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final JpaUserRepository jpauserRepository;
    private final JpaPostRepository jpaPostRepository;
    private final JpaCommentRepository jpaCommentRepository;

    public CommentEntity createComment(RequestCommentSave dto) {
        UserEntity user = getUserEntity(dto.userId());

        PostEntity post = getPostEntity(dto.postId());

        CommentEntity comment = CommentEntity.builder()
                .content(dto.content())
                .post(post)
                .user(user)
                .build();
        jpaCommentRepository.save(comment);
        return comment;
    }


    public CommentEntity EditComment(RequestCommentEdit dto) {

        CommentEntity commentEntity = getComment(dto.commentId());

        commentEntity.Edit(dto);
        jpaCommentRepository.save(commentEntity);
        return commentEntity;
    }


    public void removeComment(RequestCommentRemove dto) {
        getComment(dto.commentId());
        CommentEntity comment = jpaCommentRepository.findById(dto.commentId()).orElseThrow(() -> new IllegalArgumentException("없는 댓글 정보입니다"));
        jpaCommentRepository.delete(comment);
    }

    private CommentEntity getComment(Long commentId) {
        CommentEntity commentEntity = getCommentEntity(commentId);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String commentUserName = commentEntity.getUser().getUsername();

        if (!userName.equals(commentUserName)) {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다");
        }
        return commentEntity;
    }


    private PostEntity getPostEntity(Long postId) {
        return jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("없는 게시글 입니다."));
    }

    private UserEntity getUserEntity(Long userId) {
        return jpauserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("회원정보가 없습니다"));
    }
    private CommentEntity getCommentEntity(Long commentId) {
        return jpaCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 정보가 없습니다"));
    }

}
