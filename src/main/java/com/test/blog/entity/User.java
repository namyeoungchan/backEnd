package com.test.blog.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.extern.java.Log;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "com_user",schema = "web")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true)
    private String loginId;
    private String pw;
    private String saltCode;
    private String pw2;
    private String Authority;

    public User(final Long userId,final String loginId,final String pw,final String saltCode, final String pw2, final String Authority){
        this.userId = userId;
        this.loginId = loginId;
        this.pw = pw;
        this.saltCode = saltCode;
        this.pw2 = pw2;
        this.Authority = Authority;
    }

    public User() {

    }
}

