package com.example.noticeboard.post.ui;

import com.example.noticeboard.common.Response;
import com.example.noticeboard.post.application.dtos.RequestPostSave;
import com.example.noticeboard.post.application.dtos.ResponsePage;
import com.example.noticeboard.post.application.dtos.ResponsePostSave;
import com.example.noticeboard.post.application.service.PostService;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.application.dtos.request.RequestPostDelete;
import com.example.noticeboard.user.application.dtos.request.RequestPostEdit;
import com.example.noticeboard.user.application.dtos.response.ResponsePostEdit;
import com.example.noticeboard.user.application.dtos.response.ResponsePostSelect;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@Tag(name = "게시물 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Operation(summary = "모든 게시물을 조회")
    @GetMapping("/all")
    public ResponsePage<ResponsePostSelect> selectAllPost(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size) {
        Page<PostEntity> result = postService.findAllPost(page, size);
        Page<ResponsePostSelect> dtoPage = result.map(ResponsePostSelect::from);
        return ResponsePage.from(dtoPage);
    }

    @Operation(summary = "게시물 생성")
    @PostMapping("/save")
    public Response<ResponsePostSave> savePost(@RequestBody RequestPostSave dto) {
        PostEntity post = postService.savePost(dto);
        return Response.ok(ResponsePostSave.from(post));
    }

    @Operation(summary = "하나의 게시물을 읽기")
    @GetMapping("/{postId}")
    public Response<ResponsePostSelect> selectPost(@PathVariable Long postId) {
        PostEntity post = postService.findPost(postId);
        return Response.ok(ResponsePostSelect.from(post));
    }

    @Operation(summary = "게시물 내용 수정")
    @PatchMapping("/update")
    public Response<ResponsePostEdit> editSave(@RequestBody RequestPostEdit dto) {

        PostEntity postEntity = postService.editPost(dto);
        return Response.ok(ResponsePostEdit.from(postEntity));
    }

    @Operation(summary = "게시물 삭제")
    @DeleteMapping("/{postId}/delete")
    public Response<Void> removePost(@PathVariable Long postId, @RequestBody RequestPostDelete passwd) {
        postService.removePost(postId, passwd);
        return Response.ok(null);
    }
}
