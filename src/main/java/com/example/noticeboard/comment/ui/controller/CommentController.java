package com.example.noticeboard.comment.ui.controller;

import com.example.noticeboard.comment.application.RequestCommentSave;
import com.example.noticeboard.comment.application.service.CommentService;
import com.example.noticeboard.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public Response<Long> createComment(@RequestBody RequestCommentSave dto) {
        Long commentId = commentService.createComment(dto);
        return Response.ok(commentId);

    }

}
