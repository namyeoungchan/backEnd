package com.test.blog.User.Repository;

import com.test.blog.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByLoginId(String loginId);
}
