//package com.example.noticeboard.post.ui;
//
//import com.example.noticeboard.post.application.service.PostService;
//import com.example.noticeboard.post.domain.PostStatus;
//import com.example.noticeboard.post.repository.JpaPostRepository;
//import com.example.noticeboard.post.repository.entity.PostEntity;
//import com.example.noticeboard.user.repository.JpaUserRepository;
//import com.example.noticeboard.user.repository.entity.UserEntity;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class PostControllerTest {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private JpaPostRepository jpaPostRepository;
//    @Autowired
//    private JpaUserRepository jpaUserRepository;
//    @Test
//    @DisplayName("모든 게시글 조회")
//    void selectAllPost() throws InterruptedException {
//
//        UserEntity user = createUser(1L, "1321321!!");
//        UserEntity user2 = createUser(2L, "1321321!!");
//        UserEntity user3 = createUser(3L, "1321321!!");
//        UserEntity user4 = createUser(4L, "1321321!!");
//
//        jpaUserRepository.saveAll(List.of(user, user2, user3));
//        PostEntity first = createPost(user, "첫번째 게시글");
//        PostEntity twice = createPost(user2, "두번쨰 게시글");
//        PostEntity third = createPost(user3, "세번째 게시글");
//        jpaPostRepository.saveAll(List.of(first, twice));
//
//        Thread.sleep(2000);
//
//        jpaPostRepository.save(third);
//
//        List<PostEntity> allPost = postService.findAllPost(1,10);
//
//        assertThat(allPost).hasSize(3);
//        Assertions.assertThat(allPost.get(0).getContent())
//                .isEqualTo("세번째 게시글");
//
//    }
//    public UserEntity createUser(Long id,String passwd) {
//        return UserEntity.builder()
//                .id(id)
//                .passwd(passwd)
//                .build();
//    }
//    public PostEntity createPost(UserEntity user,String content) {
//        return PostEntity.builder()
//                .user(user)
//                .postStatus(PostStatus.NEW)
//                .content(content)
//                .build();
//    }
//}