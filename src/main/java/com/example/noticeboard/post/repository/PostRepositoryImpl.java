package com.example.noticeboard.post.repository;

import com.example.noticeboard.post.domain.interfaces.PostRepository;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final JpaPostRepository jpaPostRepository;

}
