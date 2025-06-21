package com.example.noticeboard.comment.repository.entity;

import com.example.noticeboard.common.BaseEntity;
import com.example.noticeboard.post.repository.entity.PostEntity;
import com.example.noticeboard.user.repository.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment_table")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private String content;

    public CommentEntity(UserEntity user, PostEntity post, String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }
}
