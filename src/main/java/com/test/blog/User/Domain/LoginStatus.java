package com.test.blog.User.Domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name ="LoginStatus" ,schema = "web")
public class LoginStatus {

    @Id
    private Long userId;
    @ColumnDefault("0")
    private int failCnt;
    private boolean block;
    @CreationTimestamp
    private Date lastTry;

    public LoginStatus(final Long userId,final int failCnt,final boolean block , final Date lastTry){
        this.userId = userId;
        this.failCnt = failCnt;
        this.block = block;
        this.lastTry = lastTry;

    }

}
