package com.example.noticeboard.service;

import com.example.noticeboard.DTO.CommentDTO;
import com.example.noticeboard.entity.CommentEntity;
import com.example.noticeboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void save(CommentDTO commentDTO) {
        CommentEntity.toSaveEntity(commentDTO);
    }
}
