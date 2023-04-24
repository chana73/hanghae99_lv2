package com.example.post_hanghae.controller;

import com.example.post_hanghae.dto.MsgResponseDto;
import com.example.post_hanghae.dto.PostRequestDto;
import com.example.post_hanghae.dto.PostResponseDto;
import com.example.post_hanghae.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //JSON 형식으로 내리는 것
@RequiredArgsConstructor
@RequestMapping("/api") // URL에서 공통된 api 담아주는 것
public class PostController {

    private final PostService postService; // 외부에서 절대 건들이지 못하도록 private!

    // 게시글 작성
    @PostMapping("/post")
    public PostResponseDto creatPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) { //RequestBody : Http method 안의 body값을 넣어줌 | HttpServletRequest request : ID 토큰값 불러옴
        return postService.createPost(postRequestDto, request);
    }

    //게시글 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    //게시글 선택조회

    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    //게시글 수정
    @PutMapping("/post/{id}") // public 에는 updatePost 이고 return 값은 update ???
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) { //@RequestBody : http의 요청이 그대로 적힘
        return postService.update(id, postRequestDto, httpServletRequest);
    }


    //게시글 삭제
    @DeleteMapping("/post/{id}")
    public MsgResponseDto deleteAll(@PathVariable Long id, HttpServletRequest request) {
        return postService.deleteAll(id,request);
    }
}