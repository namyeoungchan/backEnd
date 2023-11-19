package com.test.blog.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.File;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name ="Board" ,schema = "web")
public class Board {

    @Id
    private Long bno;
    @ManyToOne
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private User userid;
    private String title;
    private String content;
    private File file;
    private String writer;
    private Date createDate;


}
