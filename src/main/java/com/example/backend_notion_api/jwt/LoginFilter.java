package com.example.backend_notion_api.jwt;

import com.example.backend_notion_api.domain.CustomUserDetails;
import com.example.backend_notion_api.domain.response.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@AllArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("userId : "+username);

        //스프링 시큐리티에서 userId와 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        // 1. UserDetails에서 사용자 정보 추출
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();

        // 2. 권한(Role) 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 3. JWT 토큰 생성 (유효시간: 1시간)
        String token = jwtUtil.createJwt(username, role, 60 * 60 * 1000L);

        // 4. Authorization 헤더에 JWT 토큰 추가
        response.addHeader("Authorization", "Bearer " + token);

        // 5. Api 응답을 위한 JSON 데이터 생성
        Api<String> apiResponse = Api.<String>builder()
                .resultCode("200")
                .resultMessage("로그인을 성공하였습니다. username : "+username)
                .data(token)
                .build();

        // 6. 응답의 Content-Type 설정 (JSON 형식)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 7. Api 객체를 JSON으로 변환 후 응답에 작성
        ObjectMapper objectMapper = new ObjectMapper();
        ServletOutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, apiResponse);
        out.flush();
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        // 1. Api 객체 생성
        Api<Void> apiResponse = Api.<Void>builder()
                .resultCode("401")
                .resultMessage("로그인을 실패하였습니다.")
                .data(null)
                .build();

        // 2. 응답 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 3. 응답 헤더 설정 (JSON 형식으로 응답)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 4. Api 객체를 JSON으로 변환 후 응답에 작성
        ObjectMapper objectMapper = new ObjectMapper();
        ServletOutputStream out = response.getOutputStream();
        objectMapper.writeValue(out, apiResponse);
        out.flush();
    }
}