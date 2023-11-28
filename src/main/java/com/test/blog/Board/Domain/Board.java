package com.test.blog.Board.Domain;


import com.test.blog.User.Domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

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
