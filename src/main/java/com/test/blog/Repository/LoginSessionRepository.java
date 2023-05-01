package com.test.blog.Repository;


import com.test.blog.entity.LoginSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSessionRepository extends JpaRepository<LoginSession,Long> {
}
