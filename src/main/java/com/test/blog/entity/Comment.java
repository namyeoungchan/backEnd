package com.test.blog.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name="comment",schema ="web")
public class Comment {
    @Id
    private Long cid;
    @ManyToOne
    @JoinColumn(name = "bno" , insertable = false,updatable = false)
    private Board bno;
    @ManyToOne
    @JoinColumn(name ="user_id",insertable = false,updatable = false)
    private User userId;
    private String comment;
    private Date createDate;
    private Long parentComment;
}
