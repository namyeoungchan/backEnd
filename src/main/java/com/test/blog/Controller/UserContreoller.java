package com.test.blog.Controller;

import com.test.blog.Service.UserService;
import com.test.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpHeaders;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserContreoller {

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
    public Map<String, Object>Login(HttpServletRequest request,@RequestBody Map<String,Object>User){
        Map<String,Object> result = new HashMap<>();

        Map<String,Object> loginInfo = new HashMap<>();
        System.out.println(User.get("username"));
        System.out.println(User.get("password"));
        loginInfo.put("loginId",User.get("username"));
        loginInfo.put("loginPw",User.get("password"));
        result = userservice.login(loginInfo);
        return result;
    }
    @GetMapping("/home")
    public Map<String, Object>Home(@RequestBody Map<String,Object>User){
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> loginInfo = new HashMap<>();

        loginInfo.put("loginId",User.get("username"));
        loginInfo.put("loginPw",User.get("password"));

        result = userservice.login(loginInfo);

        return result;
    }
    @GetMapping("/chkSession")
    public Map<String, Object>chkStatus(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String sessionId = request.getSession().toString();
        System.out.println(sessionId);
        result = userservice.chkSession(sessionId);
        return result;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, WebAuthenticationDetails details) throws ServletException {
        request.logout();
        return "redirect:/login";
    }

}
