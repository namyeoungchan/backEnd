package com.test.blog.User.Service;


import com.test.blog.User.Repository.LoginRepository;
import com.test.blog.User.Repository.LoginSessionRepository;
import com.test.blog.User.Repository.UserRepository;
import com.test.blog.common.AESEncrypt;
import com.test.blog.common.ValidationChk;
import com.test.blog.User.Domain.LoginSession;
import com.test.blog.User.Domain.LoginStatus;
import com.test.blog.User.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ValidationChk validationChk;
    @Autowired
    private AESEncrypt aesEncrypt;
    @Autowired
    private  LoginSessionRepository loginSessionRepository;


    public Map<String, Object> saveUser(User user) {
        Map <String, Object> result = new HashMap<>();
        String chkUserId = user.getLoginId();
        String chkPw = user.getPw();

        if(validationChk.isUpper(chkUserId)){
            result.put("result","false");
            result.put("code","400");
            result.put("message","대문자는 사용할 수 없습니다.");
            return result;
        }else if(validationChk.isSpc(chkUserId)){
            result.put("result","false");
            result.put("code","400");
            result.put("message","특수문자는 사용할 수 없습니다.");
            return result;
        }else if(chkUserId.length()<4){
            result.put("result","false");
            result.put("code","400");
            result.put("massage","아이디는 5자 이상 이어야합니다.");
            return result;
        }
        if(validationChk.isSpc(chkPw)==false){
            result.put("result","false");
            result.put("code","400");
            result.put("message","비밀번호는 특수문자를 포함해야됩니다.");
            return result;
        }else if(chkPw.length()<8){
            result.put("result","false");
            result.put("code","400");
            result.put("message","비밀번호는 8글자 이상 입력해주세요.");
            return result;
        }
   /****패스워드 암호화 ********/
        Map<String,Object> password = new HashMap<>();
        password = aesEncrypt.aesEncrypt(user);
        User userpw = User.builder().loginId(user.getLoginId()).pw(password.get("encPw").toString()).saltCode(password.get("salt").toString()).build();

   /****!패스워드 암호화 *******/
        try{

            userRepository.save(userpw);
        }catch(Exception e){
            System.out.println(e);
            result.put("result","fail");
            result.put("code","400");
            result.put("message",e);
            return result;
        }

            result.put("result","success");
            result.put("code","200");
            result.put("LoginID",user.getLoginId());
            result.put("UserID",user.getUserId());
            result.put("message","유저가 생성되었습니다.");
        return result;
    }
    public boolean chkUser(User user){

        User chkUser = userRepository.findByLoginId(user.getLoginId());
        boolean result = false;
        if(chkUser==null){
            result =true;
        }else{
            result = false;
        }

        return result;
    }
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    public void deleteSessioinId(String sessionId) {
        loginSessionRepository.deleteBySessionId(sessionId);
    }

    public Map<String,Object> chkSession(Map<String,Object> sessionId){
        Map<String,Object> result = new HashMap<>();
        Optional<LoginSession> chkUser = Optional.of(new LoginSession());
         chkUser = Optional.ofNullable(loginSessionRepository.findBySessionId((String) sessionId.get("sessionId")));
        if(chkUser.isEmpty()){
            result.put("result","fail");
            result.put("code","04");
        } else{
            result.put("result","success");
            result.put("code","01");
        }

        return result;
    }


    public Map<String,Object> login(User user){
        Map<String,Object>result = new HashMap<>();
        User chkUser = userRepository.findByLoginId(user.getLoginId());
        if(chkUser==null){
            result.put("result","fail");
            result.put("code","04");
            result.put("message","없는회원입니다.");
            return result;
        }

        Optional<LoginStatus> loginStatus = loginRepository.findByUserId(user.getUserId());

        /**아이디 블락 여부 체크**/
        try{
            if(loginStatus.get().isBlock()){
            result.put("result","fail");
            result.put("code","400");
            result.put("message","비활성화된계정입니다.");
            return result;
        }
        }catch(Exception e){
            System.out.println(e);
        }
        User chkPw =  User.builder().loginId(user.getLoginId()).pw(user.getPw()).saltCode(chkUser.getSaltCode()).build();
        if(aesEncrypt.chkPw(chkPw).equals(chkUser.getPw())){
            try{loginRepository.deleteLoginStatus(loginStatus.get().getUserId());
            }catch (Exception e){
                System.out.println(e);
            }
            try{
                // 인증 객체 생성

                // 세션 설정
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = requestAttributes.getRequest();
                HttpSession session = request.getSession(true); // 세션을 가져오거나 새로 생성합니다.
                // 세션에 인증 객체 설정
//                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                String sessionId = session.getId();
                String ipAddress = request.getRemoteAddr();
                LoginSession userDetail = new LoginSession().builder().userId(chkUser.getUserId()).sessionId(sessionId).loginIp(ipAddress).build();
                loginSessionRepository.save(userDetail);
                result.put("sessionId",sessionId);
                /**로그인 성공 인증 권한 부여 필요**/

            }catch (Exception e){
                System.out.println(e);
            }

            result.put("result","success");
            result.put("code","200");
            result.put("message","로그인 성공");
            return result;
        }else{
            /**로그인 실패시 로직**/
            if(loginStatus.isEmpty()){
                LoginStatus failInfo = LoginStatus.builder().userId(user.getUserId()).build();
                loginRepository.save(failInfo);
                result.put("result","fail");
                result.put("code","400");
                result.put("message","로그인 실패(5회 초과 시 비활성화됩니다.)");
                return result;
            }else {
                int failCnt = loginStatus.get().getFailCnt()+1;
                if(failCnt>=5){
                    loginRepository.updateBlockChk(user.getUserId());
                }
                if(loginStatus.get().isBlock()){
                    result.put("result","fail");
                    result.put("code","400");
                    result.put("message","비활성화된계정입니다.");
                    return result;
                }
                loginRepository.updateFailCnt(loginStatus.get().getUserId());
                result.put("result","fail");
                result.put("code","400");
                result.put("message","로그인 실패(5회 초과 시 비활성화됩니다.) 현재 실패 횟 수 :"+failCnt);
                return result;
            }
            /**블락시 5분 카운트**/

        }



    }

}
