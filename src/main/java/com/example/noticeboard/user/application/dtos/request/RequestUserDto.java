package com.example.noticeboard.user.application.dtos.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestUserDto(
        @Size(min = 4, max = 10, message = "4자 이상 10자 이하로 입력해주세요.")
        @Pattern(regexp = "^[a-z0-9]+$", message = "알파벳 소문자(a~z)와 숫자(0~9)만 사용할 수 있습니다.")
        String userName,
        @Size(min = 8, max = 15, message = "8자 이상 15자 이하로 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()_+\\-={}\\[\\]:\";'<>?,./~`|\\\\]+$",
                message = "알파벳 대소문자, 숫자, 특수문자만 사용할 수 있습니다.")
        String passwd) {
}
