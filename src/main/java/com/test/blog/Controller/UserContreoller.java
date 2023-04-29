package com.test.blog.Controller;

import com.test.blog.Service.UserService;
import com.test.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class UserContreoller {

    @Autowired
    private UserService userservice;

    @RequestMapping("/signIn")
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

    @RequestMapping("/login")
    public Map<String, Object>Login(@RequestBody Map<String,Object>User){
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> loginInfo = new HashMap<>();

        loginInfo.put("loginId",User.get("loginId"));
        loginInfo.put("loginPw",User.get("loginPw"));
        result = userservice.login(loginInfo);

        return result;
    }

}
