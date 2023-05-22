package com.test.blog.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@Table(name ="Board" ,schema = "web")
public class Board {

    @Id
    private Long nId;
    private String title;
    private String content;
//    @ManyToOne("")
//    private Long userId;
    @CreationTimestamp
    private Date writeDate;


}
