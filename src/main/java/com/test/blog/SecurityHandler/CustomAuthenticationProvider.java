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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService; // 사용자 정보를 가져오는 서비스 클래스

    @Autowired
    private LoginSessionRepository loginSessionRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {


        String username = authentication.getName();
        String password = authentication.getCredentials().toString();


        // 사용자 정보를 DB에서 가져와서 인증 처리
        User user = new User();
            user = userService.providerChkUser(username);
        System.out.println("username");
        System.out.println(username);
//             user = userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username/password"));
            Map <String , Object> loginInfo = new HashMap<>();
            loginInfo.put("loginId",username);
            loginInfo.put("loginPw",password);
            Map<String ,Object> result = new HashMap<>();
            result = userService.login(loginInfo);
        System.out.println(result.get("result"));
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

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(user, username,authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
