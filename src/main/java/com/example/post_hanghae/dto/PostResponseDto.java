package com.example.post_hanghae.dto;

import com.example.post_hanghae.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PostResponseDto { // 내가 원하는 것만 보여주기 위해
   // 필드 값
    private Long id;
    private String username; //final - 한번 정하면 절대 변하지 않음 (상수) 왜 final 쓰면 빨간줄이지?
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    // private final Stirng password; ->password는 보여주지 않겠다!!

//    private String msg;



    //생성자
    public PostResponseDto(Post post) { // -> 내부에서 생성자만 건들이도록 public 해줌
        this.id = post.getId();
        this.username = post.getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
//        this.msg = msg;
    }
}