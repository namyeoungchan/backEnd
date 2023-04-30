package com.test.blog.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
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


}
