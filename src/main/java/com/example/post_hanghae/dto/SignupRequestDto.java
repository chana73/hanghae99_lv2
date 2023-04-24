package com.example.post_hanghae.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class SignupRequestDto {

    //Pattern으로 영문자(소문자),숫자만 입력 그리고 4글자~10글자로 입력해야 하게 만듦
//    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "id는 소문자, 숫자만 가능합니다.")
//    @NotNull(message = "id를 입력해주세요")
    private String username;

//    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "password는 영문, 숫자만 가능합니다.")
//    @NotNull(message = "password를 입력해주세요")
    private String password;
    private boolean admin = false;
    private String adminToken = "";
}
