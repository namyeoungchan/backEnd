package com.test.blog.Controller;

import com.test.blog.Service.UserService;
import com.test.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userservice;

    @PostMapping("/signIn")
    public Map<String, Object> SignIn(@RequestBody Map<String,Object>User){
        User userInfo = new User();
        Map<String, Object> result = new HashMap<>();
        userInfo.setLoginId(User.get("userId").toString());
        userInfo.setPw(User.get("userPw").toString());
        boolean chk = userservice.chkUser(userInfo);;
        if(chk){
            System.out.println("chk");
            System.out.println(chk);
            result =userservice.saveUser(userInfo);

            return result;
        }else{
            System.out.println("testast");
            result.put("result","error");
            result.put("code","400");
            result.put("message","이미 존재하는 아이디입니다.");
            return result;
        }
    }


    @PostMapping("/loginUser")
    public ResponseEntity<Map<String, Object>> Login(@RequestBody Map<String,Object>User){
        Map<String,Object> result = new HashMap<>();
        // 응답 헤더에 세션 ID 추가
        HttpHeaders headers = new HttpHeaders();
        Map<String,Object> loginInfo = new HashMap<>();
        System.out.println(User.get("username"));
        System.out.println(User.get("password"));
        loginInfo.put("loginId",User.get("username"));
        loginInfo.put("loginPw",User.get("password"));
        result = userservice.login(loginInfo);
        headers.add("Set-Cookie", "JSESSIONID=" + result.get("sessionId") + "; Path=/; Secure; HttpOnly");
        System.out.println(result.get("sessionId"));

        return ResponseEntity.ok().headers(headers).body(result);
    }

    @GetMapping("/chkSession")
    public Map<String, Object>chkStatus(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        // WebAuthenticationDetails를 사용하여 세션 ID 확인
//        System.out.println(request.getSession().getId());
//        String sessionId = ((WebAuthenticationDetails) authentication.getDetails()).getSessionId();
//        result = userservice.chkSession(sessionId);
//        System.out.println(result);
        return result;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/login";
    }

}
