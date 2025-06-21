package com.example.noticeboard.comment.repository;

import com.example.noticeboard.comment.repository.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<CommentEntity,Long> {
}
