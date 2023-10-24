package com.test.blog.Filter;

import com.test.blog.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TokenAuthenticationFilter extends OncePerRequestFilter {


    private UserService userService;

    public TokenAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 요청 URL 확인
        String requestUrl = request.getRequestURI();

        // 특정 URL을 거치지 않도록 설정
        System.out.println(requestUrl);
        if ("/loginUser".equals(requestUrl) ||"/signIn".equals(requestUrl)||"/favicon.ico".equals(requestUrl)
                ||"/login".equals(requestUrl)||"/swagger-ui.html".equals(requestUrl)) {
            // 특정 URL은 필터를 거치지 않고 진행
            filterChain.doFilter(request, response);
            return;
        }

        Map<String , Object> result = new HashMap<>();
        String sessionId = "";
        // 요청 헤더에서 Authorization 세션 아이디 추출
        try{

             sessionId = request.getHeader("Authorization");
            sessionId = sessionId.substring(sessionId.lastIndexOf(" ")+1);
            System.out.println("sessionId::::::"+sessionId);
            result = userService.chkSession(sessionId);
            System.out.println("sessionAuth");
            System.out.println(result.get("result"));
            if (result.get("result").equals("success")){
                // 인증 성공
                // SecurityContextHolder에 인증된 세션 저장
                Authentication authentication = new UsernamePasswordAuthenticationToken(sessionId, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
//                filterChain.doFilter(request, response);
            } else {
                // 인증 실패
                // SecurityContextHolder의 인증 정보 제거
                System.out.println("인증실패 ::::::::");
                System.out.println(sessionId);
                SecurityContextHolder.clearContext();
            }
        }catch(Exception e){
//            SecurityContextHolder.clearContext();
            System.out.println("세션인증 오류입니다. :::TokenAuthenticationFilter Error"+e);
        }

        filterChain.doFilter(request, response);
    }
}
