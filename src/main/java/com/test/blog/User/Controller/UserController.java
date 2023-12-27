package com.test.blog.User.Controller;

import com.test.blog.User.Service.UserService;
import com.test.blog.User.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userservice;

    @PostMapping("/signIn")
    public Map<String, Object> SignIn(@RequestBody User user){
        User userInfo = User.builder()
                .loginId(user.getLoginId())
                .pw(user.getPw())
                .build();
        Map<String, Object> result = new HashMap<>();
        boolean chk = userservice.chkUser(userInfo);;
        if(chk){
            result =userservice.saveUser(userInfo);
            return result;
        }else{
            result.put("result","error");
            result.put("code","400");
            result.put("message","이미 존재하는 아이디입니다.");
            return result;
        }
    }


    @PostMapping("/loginUser")
    public ResponseEntity<Map<String, Object>> Login(@RequestBody User user){
        Map<String,Object> result = new HashMap<>();
        // 응답 헤더에 세션 ID 추가
        HttpHeaders headers = new HttpHeaders();
        result = userservice.login(user);
        headers.add("Set-Cookie", "JSESSIONID=" + result.get("sessionId") + "; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok().headers(headers).body(result);
    }

    @PostMapping("/chkSession")
    public Map<String, Object>chkStatus(HttpServletRequest request, @RequestBody Map<String,Object> sessionId){
        Map<String,Object> result = new HashMap<>();
        result = userservice.chkSession(sessionId);
        System.out.println(result);
        return result;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        userservice.deleteSessioinId(request.getSession().getId());
        return "redirect:/login";
    }

}
