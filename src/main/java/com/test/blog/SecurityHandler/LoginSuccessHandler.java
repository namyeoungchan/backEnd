package com.test.blog.SecurityHandler;

import com.test.blog.Repository.LoginSessionRepository;
import com.test.blog.entity.LoginSession;
import com.test.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 시 수행할 작업을 구현합니다.
        // 예를 들어, 로그인 이력을 기록한다던가, 로그인 사용자 정보를 세션에 저장한다던가, 등등...
        HttpSession httpSession = request.getSession(false); //세션이 있는 페이지에서 넘어왔을 경우를 확인해야함
//        HttpSession httpSession = request.getSession();//세션이 있는 페이지에서 넘어왔을 경우를 확인해야함

        // 세션 정보별 처리
//        if(httpSession != null) { // 세션이 있는 페이지에서 넘어 왔을경우 기존 Dummy 세션을 지우고 새롭게 발급 받는다
//            httpSession.invalidate();
//        }
        WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        String ipAddress = details.getRemoteAddress();
        System.out.println("로그인 성공!");
        httpSession = request.getSession(true);
        httpSession.setAttribute("userId", authentication.getPrincipal());
        httpSession.setAttribute("sessionId", httpSession.getId());


        // 로그인 성공 후 리다이렉트할 URL을 지정합니다.
        String targetUrl = "/swagger-ui.html#/user-contreoller/testUsingPOST";
//        System.out.println(httpSession.);
        // 지정한 URL로 리다이렉트합니다.
        response.sendRedirect(targetUrl);
    }

}
