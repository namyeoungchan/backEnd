package com.test.blog.SecurityHandler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 수행할 작업을 구현합니다.
        // 예를 들어, 로그인 이력을 기록한다던가, 로그인 사용자 정보를 세션에 저장한다던가, 등등...

        HttpSession session = request.getSession();
        System.out.println(session);
        // 로그인 성공 후 리다이렉트할 URL을 지정합니다.
        String targetUrl = "/home";
        // 지정한 URL로 리다이렉트합니다.
        response.sendRedirect(targetUrl);
    }

}
