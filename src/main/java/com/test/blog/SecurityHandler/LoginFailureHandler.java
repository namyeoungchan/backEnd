package com.test.blog.SecurityHandler;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패 시 처리할 로직 작성
        HttpSession session = request.getSession();
        System.out.println(session);
        System.out.println("request"+request);
        System.out.println("exception"+exception);
        System.out.println("response"+response);
        response.sendRedirect("/login?error=true");
    }
}
