package com.test.blog.entity;


import lombok.*;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

@Entity
@Data
@Table(name= "goods_board",schema = "web")
public class GoodsBoard {

    @Id
    private Long ino;
    @ManyToOne
    @JoinColumn(name="user_id",insertable = false,updatable = false)
    private User userId;
    private String category;
    private String content;
    private int prise;
    private Date creatDate;
    private File image;
}
