package com.test.blog.entity;


import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private String Authority;



}

