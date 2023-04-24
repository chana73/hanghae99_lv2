package com.example.post_hanghae.entity;

import com.example.post_hanghae.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends TimeStamped{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // id 자동 증가
    private Long id;
    @Column(nullable = false) // 반드시 적어라
    private String username;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
//    @Column(nullable = false)
//    private String password;  //Lv1 에서 사용한 password
//    @Column(nullable = false)
//    private Long userid;



    // 게시글 작성
    public Post(PostRequestDto postRequestDto, String username) { //이미 로그인이 된 상태에서 게시글 작성을 하기 때문에 따로 id/password 안적어도됨
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.username = username;
//        this.userid = getUserid();
//        this.password = postRequestDto.getPassword();
    }
    // 선택한 글 수정(변경)
    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
//        this.password = postRequestDto.getPassword();
    }
}