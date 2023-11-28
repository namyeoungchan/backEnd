package com.test.blog.User.Domain;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Builder
@Entity
@Table(name = "LoginSession" ,schema = "web")
public class LoginSession {

    @Id
    private Long userId;
    private String sessionId;
    private String loginIp;

    public LoginSession(final Long userId,final String sessionId,final String loginIp){
        this.loginIp = loginIp;
        this.sessionId = sessionId;
        this.userId = userId;

    }

    public LoginSession() {

    }
}
