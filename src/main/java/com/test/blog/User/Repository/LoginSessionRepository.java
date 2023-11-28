package com.test.blog.User.Repository;


import com.test.blog.User.Domain.LoginSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSessionRepository extends JpaRepository<LoginSession,Long> {
    LoginSession findBySessionId(String SessionId);

    public static void deleteBySessionId(String SessionId) {

    }
}
