package com.example.post_hanghae.dto;

import com.example.post_hanghae.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor //파라미터 없는 기본생성자 만들어줌

public class PostRequestDto {
//    private String username;
    private String title;
    private String content;
//    private String userid;
//    private String password; // 문자열일 수도 있으니 Long 보다 String 이 더 효율적?
//    private UserRoleEnum role; ->굳이 지금은 필요가 없음
}