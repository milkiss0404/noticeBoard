package com.example.noticeboard.post.repository.entity;

import com.example.noticeboard.comment.repository.entity.CommentEntity;
import com.example.noticeboard.common.BaseEntity;
import com.example.noticeboard.post.domain.Post;
import com.example.noticeboard.post.domain.PostStatus;
import com.example.noticeboard.user.repository.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "post_table")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String passwd;
    private String title;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    @OneToMany(mappedBy = "post",fetch = FetchType.LAZY,cascade = CascadeType.ALL,
            orphanRemoval = true)//postEntity 에서 삭제해도 DB 에서 삭제처리됨
    private List<CommentEntity> comments;
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    public Post toPost() {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .user(user)
                .postStatus(PostStatus.NEW)
                .build();
    }

    public PostEntity(String passwd, String title,String content, UserEntity user) {
        this.passwd = passwd;
        this.title = title;
        this.content = content;
        this.user = user;
        this.postStatus = PostStatus.NEW;
    }
    public void edit(String passwd, String title, String content) {
        this.passwd = passwd;
        this.title = title;
        this.content = content;
        this.postStatus = PostStatus.EDIT;
    }
}
