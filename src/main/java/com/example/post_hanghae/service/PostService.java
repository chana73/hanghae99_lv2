package com.example.post_hanghae.service;

import com.example.post_hanghae.dto.MsgResponseDto;
import com.example.post_hanghae.dto.PostRequestDto;
import com.example.post_hanghae.dto.PostResponseDto;
import com.example.post_hanghae.entity.Post;
import com.example.post_hanghae.entity.User;
import com.example.post_hanghae.jwt.JwtUtil;
import com.example.post_hanghae.repository.PostRepository;
import com.example.post_hanghae.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor //fianl이 붙거나 @NotNull이 붙은 필드의 생성자를 자동 생성
public class PostService {

    public static final String SUBJECT_KEY = "sub";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    //@Autowired 를 안쓰는 이유 -> private "final"을 꼭 써야하기 때문!(안전)
    // 게시글 작성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request); // JWT 안에 있ㄴ는 정보를 담는 clams 객체
        Claims claims;

        if (token == null) {
            return null;
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
        );
//        String title = postRequestDto.getTitle();
//        String content = postRequestDto.getContent();
//        String username = user.getUsername();
//
//        Post newPost = new Post(title, content, username);
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user.getUsername()));

        return new PostResponseDto(post);
    }

        /* 1번방법
            public MsgResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        Post post = new Post(postRequestDto);
        String token = jwtUtil.resolveToken(request); // request에서 토큰정보를 빼와서 String token변수에 저장.
        Claims claims;

        if (jwtUtil.validateToken(token)) {// 토큰의 유효성 검사
            claims = jwtUtil.getUserInfoFromToken(token);// 토큰으로부터 유저의 정보를 받아옴
            post.setUsername(claims.getSubject());
            postRepository.save(post);
            return new MsgResponseDto("작성 성공", HttpStatus.OK);
        } else {
            return new MsgResponseDto("작성 실패", HttpStatus.BAD_REQUEST);
        }
         */


//Lv1에서 썼던 것
//        Post post = new Post(postRequestDto);
//        postRepository.save(post);
//
//        // 이곳에 성공여부 체크 로직 있어야함.
//        return new PostResponseDto(post);


    // 전체 게시글 목록 조회
    @Transactional(readOnly = true) // 왜 안되ㅣ지? ->springframework에 있는 Transactional
    public List<PostResponseDto> getPosts() {
        List<Post> posts = postRepository.findAllByOrderByModifiedAtDesc();

        return posts.stream().map(PostResponseDto::new).toList();


    }
        /* stream을 안쓰고 for문을 썼을때(postList에 담김 데이터들을 객체 Post로 모두 옮김)
         List<PostResponseDto> postResponseDto = new ArrayList<>(); //postResponseDto 빈 리스트 통 만들기
         for (Post post : postList) {
          postResponseDto.add(new PostResponseDto(post));
          }
           return postResponseDto;
        */

    // 선택한 게시글 조회
    @Transactional(readOnly = true) // 왜 안되ㅣ지? -> 위에 해결방법 적음
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new NullPointerException("아이디가 존재하지 않습니다."));

        return new PostResponseDto(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto update(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 게시글입니다.")
        );
        String token = jwtUtil.resolveToken(request); //jwt 안에 있는 정보를 담는 claims 객체
        Claims claims;

        if (token == null) {
            return null;
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.get(SUBJECT_KEY, String.class);
        if (!StringUtils.equals(post.getUsername(), username)) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }
        post.update(postRequestDto);
        return new PostResponseDto(post);
    }
//        if (jwtUtil.validateToken(token)) {
//            String username = claims.get(SUBJECT_KEY, String.class);
//            if (StringUtils.equals(post.getUsername(), username)) {
//                // StringUtils.equals를 쓴 이유는 Null-safe하게 사용하기 위해서 -> Java가 잘못함
//                post.update(postRequestDto);
//                return new PostResponseDto(post);
//            } else {
//                return null;
//            }
//        } else {
//            return null;
//        }


    /* 다른 방법
    public PostResponseDto update(Long id, PostRequestDto postRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request); //request에서 token 가져옴
        Claims claims; //jwt 안에 있는 정보를 담는 claims 객체

        if(token!=null) {
            if(jwtUtil.validateToken(token)) { //유효한 토큰이라면 사용자 정보 가져와라
                claims = jwtUtil.getUserInfoFromToken(token);
            } else { //토근 사용안되는거면
                throw new IllegalArgumentException("Token Error");
            }
            // 가져온 토근에서 사용자 정보를 사용하여 DB조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow( // claims.getSubject : 넣어둔 username 가져오기, findByUsername으로 UserRepository에서 user 정보 가져와라
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            Post post = postRepository.fin
        }

    }
     */

    /* Lv1 게시글 수정
    public PostResponseDto update(Long id, PostRequestDto postRequestDto) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        PostResponseDto postResponseDto = new PostResponseDto(post);
        if (!post.getPassword().equals(postRequestDto.getPassword())) {
            postResponseDto.setMsg("업데이트 실패");
            return postResponseDto;
        }else {
            post.update(postRequestDto);
            postResponseDto = new PostResponseDto(post);// 위에 post는 업데이트가 안되었기 때문에
            postResponseDto.setMsg("업데이트 성공"); // 74,75번줄 순서 중요!! postResponseDto에 post를 업데이트 해버리기때문에 순서가 바뀌면 msg 값이 "Null"이 나옴
            return postResponseDto;
        }
    }
        /* -> 원래 하려고 했던 게시글 수정(100% 이해를 하지 못해서 사용X)
            @Transactional
    public ResponseEntity<?> update(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        if (memo.getPassword().equals(requestDto.getPassword())) {
            memo.update(requestDto);
            return ResponseEntity.ok(new MemoResponseDto(memo));
        } else {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
            //오류 상태코드를 입력오류니 다른 오류를 찾지 말라고 알려주는 것! 리소스 낭비 방지. 입력오류라는 뜻!
        }
    }
        */


    // 게시글 삭제
    @Transactional
    public MsgResponseDto deleteAll(Long id, HttpServletRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("게시글이 존재하지 않습니다.")
        );
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        if (token == null) {
            return null;
        }
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }
        claims = jwtUtil.getUserInfoFromToken(token);
        String username = claims.get(SUBJECT_KEY, String.class);

        if (!StringUtils.equals(post.getUsername(), username)) {
            return  new MsgResponseDto("아이디가 같지 않습니다.", HttpStatus.BAD_REQUEST);
        }
         postRepository.delete(post);
        return new MsgResponseDto("삭제 성공", HttpStatus.OK);
    }
}

    /* Lv1 게시글 삭제
    public String deleteAll(Long id, String password) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );

        if (post.getPassword().equals(password)) { //password를 string 값으로 받았기 때문에 =은 안되고 equals로 비교
            postRepository.deleteById(id);
            return "삭제 성공"; // 다른 값들은 나오지 않고 삭제성공 msg 만 return하기 위해!
        } else {
            return "비밀번호가 다릅니다.";
        }

    }
}


/* -> 원래 하려고 했던 삭제
    @Transactional
    public MemoResponseDto deleteAll(Long id, String password) {
        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글 존재하지 않습니다.")
        );

        if (memo.getPassword().equals(password)) {
            memoRepository.deleteById(id);
            return new MemoResponseDto("삭제되었습니다.", HttpStatus.OK.value());//.value 왜 쓸까?
        } else
            return new MemoResponseDto("비밀번호가 다릅니다.", HttpStatus.UNAUTHORIZED.value());//.value 왜 쓸까?
    }
}

 */