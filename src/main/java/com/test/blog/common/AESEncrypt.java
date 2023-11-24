package com.test.blog.common;
import com.test.blog.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.salt.FixedStringSaltGenerator;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;
import org.jasypt.salt.StringFixedSaltGenerator;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class AESEncrypt {

    public Map<String,Object> aesEncrypt(User pw) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        SaltGenerator saltGenerator = new RandomSaltGenerator();
        byte[] saltBytes = saltGenerator.generateSalt(16);
        config.setPassword("papar_upa");
        /**암호화 키 환경변수로 관리 할 것**/
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setStringOutputType("base64");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < saltBytes.length; i++) {
            sb.append(Integer.toString((saltBytes[i] & 0xFF) + 256, 16).substring(1));
        }

        encryptor.setConfig(config);
        encryptor.setSaltGenerator(new StringFixedSaltGenerator(sb.toString()));
        Map<String,Object> password = new HashMap<>();
        password.put("encPw",encryptor.encrypt(pw.getPw()));
        password.put("salt",sb.toString());

        return password;

    }

    public String chkPw(User user){
        // 암호화에 사용할 키 설정

        String password = "papar_upa";
        // Jasypt 설정
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations(1000);
        config.setPoolSize(1);
        config.setStringOutputType("base64");

        // 암호화 객체 생성
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setConfig(config);

        encryptor.setSaltGenerator(new StringFixedSaltGenerator(user.getSaltCode()));
        // 암호화 할 문자열
        String input = user.getPw();

        // 암호화
        String encrypted = encryptor.encrypt(input);

        // 결과 출력
        System.out.println("Input: " + input);
        System.out.println("Salt: " + new String(user.getSaltCode()));
        System.out.println("Encrypted: " + encrypted);
        return encrypted;
    }

}
