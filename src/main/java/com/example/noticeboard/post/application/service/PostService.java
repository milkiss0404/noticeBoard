package com.example.noticeboard.post.application.service;

import com.example.noticeboard.common.exception.CustomBadRequestException;
import com.example.noticeboard.post.application.dtos.RequestPostSave;
import com.example.noticeboard.post.domain.PostStatus;
import com.example.noticeboard.post.repository.JpaPostRepository;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.application.dtos.request.RequestPostDelete;
import com.example.noticeboard.user.application.dtos.request.RequestPostEdit;
import com.example.noticeboard.user.domain.UserRole;
import com.example.noticeboard.user.repository.JpaUserRepository;
import com.example.noticeboard.user.repository.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final JpaUserRepository jpaUserRepository;
    private final JpaPostRepository jpaPostRepository;
    private final PasswordEncoder passwordEncoder;

    public PostEntity savePost(RequestPostSave postRequest) {

        UserEntity user = getUserEntity(postRequest.userId());
        String encode = enCordingPasswd(postRequest.postPasswd());

        PostEntity post = PostEntity.builder()
                .user(user)
                .passwd(encode)
                .postStatus(PostStatus.NEW)
                .title(postRequest.title())
                .content(postRequest.content())
                .build();

        return jpaPostRepository.save(post);

    }
    public Page<PostEntity> findAllPost(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDateTime").descending());
        return jpaPostRepository.findAll(pageable);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public PostEntity editPost(RequestPostEdit dto) {

        PostEntity post = findPost(dto.postId());
        getPostEntity(dto.postId());
        comparisonPasswd(dto.passwd(), post);
//        extracted(dto.userId(), post);

        post.edit(
                enCordingPasswd(dto.passwd()),
                dto.title(),
                dto.content());

        return jpaPostRepository.save(post);
    }




    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void removePost(Long postId, RequestPostDelete dto) {
        PostEntity post = getPostEntity(postId);
//        extracted(dto.userId(), post);
        comparisonPasswd(dto.passwd(), post);
        jpaPostRepository.delete(post);
    }

    private PostEntity getPostEntity(Long postId) {
        PostEntity post = findPost(postId);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String postUsername = post.getUser().getUsername();
        if (!userName.equals(postUsername)) {
            throw new CustomBadRequestException("작성자만 삭제/수정할 수 있습니다");
        }
        return post;
    }

//    private static void extracted(Long userId, PostEntity post) {
//            if (!post.getUser().getId().equals(userId)) {
//                throw new CustomBadRequestException("작성자만 삭제/수정할 수 있습니다");
//            }
//    }

    private String enCordingPasswd(String passwd) {
        return passwordEncoder.encode(passwd);
    }


    public PostEntity findPost(Long postId) {
        return jpaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("없는 게시물"));
    }
    private UserEntity getUserEntity(Long userId) {
        return jpaUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는 회원"));
    }
    private void comparisonPasswd(String passwd, PostEntity post) {
        if (!passwordEncoder.matches(passwd, post.getPasswd())) {
            throw new IllegalArgumentException("게시글 비밀번호가 틀렸습니다");
        }
    }

}
