package com.example.noticeboard.comment.ui.controller;

import com.example.noticeboard.comment.application.dto.RequestCommentEdit;
import com.example.noticeboard.comment.application.dto.RequestCommentRemove;
import com.example.noticeboard.comment.application.dto.RequestCommentSave;
import com.example.noticeboard.comment.application.dto.ResponseComment;
import com.example.noticeboard.comment.application.service.CommentService;
import com.example.noticeboard.comment.repository.entity.CommentEntity;
import com.example.noticeboard.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@Tag(name = "댓글 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 생성")
    @PostMapping("/save")
    public Response<ResponseComment> createComment(@RequestBody RequestCommentSave dto) {
        CommentEntity comment = commentService.createComment(dto);
        return Response.ok(ResponseComment.from(comment));

    }
    @Operation(summary ="댓글 수정")
    @PostMapping("/edit")
    public Response<ResponseComment> editComment(@RequestBody RequestCommentEdit dto) {
        CommentEntity comment = commentService.EditComment(dto);
        return Response.ok(ResponseComment.from(comment));

    }
    @Operation(summary ="댓글 삭제")
    @DeleteMapping("/remove")
    public Response<ResponseComment> removeComment(@RequestBody RequestCommentRemove dto) {
        commentService.removeComment(dto);
        return Response.ok(null);

    }

}
