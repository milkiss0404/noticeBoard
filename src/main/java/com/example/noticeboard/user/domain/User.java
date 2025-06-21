package com.example.noticeboard.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Builder
@NoArgsConstructor
@Getter
@Validated
public class User {
    private Long id;
    private String username;
    private String passwd;

    public User(Long id,String username, String passwd) {
        this.id = id;
        this.username = username;
        this.passwd = passwd;
    }

}
