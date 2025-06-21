package com.example.noticeboard.user.ui;

import com.example.noticeboard.user.application.dtos.request.RequestUserDto;
import com.example.noticeboard.user.application.service.UserService;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserControllerTest {

    @Autowired
    UserService userService;

    @DisplayName("비밀번호 유효성 테스트")
    @Test
    void createUser() {
        RequestUserDto userAddRequestDto
                = new RequestUserDto("asds", "123");
        Assertions.assertThatThrownBy(() -> userService.createUser(userAddRequestDto))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("8자 이상 15자 이하로 입력해주세요.");
    }
}