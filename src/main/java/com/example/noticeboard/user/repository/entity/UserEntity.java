package com.example.noticeboard.user.repository.entity;

import com.example.noticeboard.common.BaseEntity;
import com.example.noticeboard.user.domain.User;
import com.example.noticeboard.user.domain.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Table(name = "user_table")
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwd;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User toUser() {
        return User.builder()
                .id(id)
                .username(username)
                .build();
    }

    public UserEntity(String username) {
        this.username = username;
    }


}
