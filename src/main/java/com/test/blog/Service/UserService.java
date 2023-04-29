package com.test.blog.Service;


import com.test.blog.Repository.LoginRepository;
import com.test.blog.Repository.UserRepository;
import com.test.blog.common.AESEncrypt;
import com.test.blog.common.ValidationChk;
import com.test.blog.entity.LoginStatus;
import com.test.blog.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private ValidationChk validationChk;
    @Autowired
    private AESEncrypt aesEncrypt;


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
//   user.setPw(aesEncrypt.encrypt(user.getPw()));
        Map<String,Object> password = new HashMap<>();
        Map<String,Object> userPw = new HashMap<>();
        userPw.put("pw",user.getPw());
         password = aesEncrypt.aesEncrypt(userPw);
         try{
             user.setPw(password.get("encPw").toString());
             user.setSaltCode(password.get("salt").toString());
         }catch (Exception e){
             System.out.println("회원가입 비밀번호 암호화");
         }
   /****!패스워드 암호화 *******/


        try{
            userRepository.save(user);
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
        Optional<User> chkUser = Optional.of(new User());;
        chkUser = userRepository.findByLoginId(user.getLoginId());
        boolean result = false;
        if(chkUser.equals(Optional.empty())){
            result =true;
        }else{
            result = false;
        }

        return result;
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public Map<String,Object> login(Map<String, Object> user){
        Map<String,Object>result = new HashMap<>();
        Optional<LoginStatus> loginStatus = Optional.of(new LoginStatus());
        String loginId = user.get("loginId").toString();
//        Map<String,Object> loginPw = new HashMap<>();
        String loginPw="";
        String userPw ="";
        Long userId;
        Optional<User> chkUser = Optional.of(new User());
        chkUser = userRepository.findByLoginId(loginId);
        if(chkUser.equals(Optional.empty())){
            result.put("result","fail");
            result.put("code","04");
            result.put("message","없는회원입니다.");
            return result;
        }else{
            userPw = chkUser.get().getPw();
            userId = chkUser.get().getUserId();
        }
        try{
//            loginPw = aesEncrypt.getEncrypt(user.get("loginPw").toString(),chkUser.get().getSaltCode().getBytes());

            loginPw = aesEncrypt.chkPw(user.get("loginPw").toString(),chkUser.get().getSaltCode());
        }catch (Exception e){
            System.out.println(e);
            System.out.println("saltCode가 존재하지않습니다.");
        }
//        loginPw = aesEncrypt.aesEncrypt(loginPw).get("encPw").toString();




        loginStatus = loginRepository.findByUserId(userId);
        /**아이디 블락 여부 체크**/
        try{
            if(loginStatus.get().isBlock()){
            result.put("result","fail");
            result.put("code","400");
            result.put("message","비활성화된계정입니다.");
            return result;
        }
        }catch(Exception e){
            System.out.println("첫 로그인 실패 :"+e);
        }
        /**블락 여부 체크**/
        /**암복호화 후 로그인 로직 추가 해야됨**/
        System.out.println("loginPw:::"+loginPw);
        System.out.println("userPw:::"+userPw);
        if(loginPw.equals(userPw)){
            /**로그인 성공 인증 권한 부여 필요**/
            try{loginRepository.deleteLoginStatus(loginStatus.get().getUserId());
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
                LoginStatus failInfo = new LoginStatus();
                failInfo.setUserId(userId);
                loginRepository.save(failInfo);
                result.put("result","fail");
                result.put("code","400");
                result.put("message","로그인 실패(5회 초과 시 비활성화됩니다.)");
                return result;
            }else {
                int failCnt = loginStatus.get().getFailCnt()+1;
                if(failCnt>=5){
                    loginRepository.updateBlockChk(userId);
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
