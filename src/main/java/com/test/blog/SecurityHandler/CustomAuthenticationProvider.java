package com.test.blog.SecurityHandler;

import com.test.blog.Repository.LoginSessionRepository;
import com.test.blog.Service.UserService;
import com.test.blog.entity.LoginSession;
import com.test.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService; // 사용자 정보를 가져오는 서비스 클래스

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username ="";
        String password ="";

        try{
            System.out.println("그니까 왜 반복해");
            username=authentication.getPrincipal().toString();
            password=authentication.getCredentials().toString();

   }catch (Exception e){
            System.out.println("몇번째 반복하냐"+e);
   }

        // 사용자 정보를 DB에서 가져와서 인증 처리
        User user = new User();
            user = userService.providerChkUser(username);

//             user = userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username/password"));
            Map <String , Object> loginInfo = new HashMap<>();
            loginInfo.put("loginId",username);
            loginInfo.put("loginPw",password);
            if(userService.login(loginInfo).get("code")=="200"){
                LoginSession userDetail = new LoginSession();
                WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
                String sessionId = details.getSessionId();
                String ipAddress = details.getRemoteAddress();
                userDetail.setUserId(user.getUserId());
                userDetail.setSessionId(sessionId);
                userDetail.setLoginIp(ipAddress);
                loginSessionRepository.save(userDetail);
            }else{
                throw new BadCredentialsException("Invalid username/password");
            }



        // 사용자 정보가 맞으면 인증 완료 후 Authentication 객체 반환
        return new UsernamePasswordAuthenticationToken(user, username,authentication.getAuthorities());
//        System.out.println("");
//        return new UsernamePasswordAuthenticationToken(user,authentication.getCredentials());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
