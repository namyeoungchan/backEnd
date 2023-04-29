package com.test.blog.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name ="LoginStatus" ,schema = "web")
public class LoginStatus {

    @Id
    private Long userId;
    @ColumnDefault("0")
    private int failCnt;
    private boolean block;
    @CreationTimestamp
    private Date lastTry;



}
