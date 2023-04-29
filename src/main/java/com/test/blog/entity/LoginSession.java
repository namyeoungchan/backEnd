package com.test.blog.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "LoginSession" ,schema = "web")
public class LoginSession {

    @Id
    private Long userId;
    private String sessionId;
    private String loginIp;


}
