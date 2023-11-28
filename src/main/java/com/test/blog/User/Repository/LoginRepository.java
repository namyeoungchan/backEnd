package com.test.blog.User.Repository;


import com.test.blog.User.Domain.LoginStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<LoginStatus,Long> {
    Optional<LoginStatus> findByUserId(Long userId);
    @Modifying
    @Transactional
    @Query("UPDATE LoginStatus l SET l.failCnt = l.failCnt + 1 ,l.lastTry = now() WHERE l.userId = :userId")
    void updateFailCnt(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE LoginStatus l SET l.block = true,l.failCnt = l.failCnt + 1 ,l.lastTry = now() WHERE l.userId = :userId")
    void updateBlockChk(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("delete from LoginStatus l WHERE l.userId= :userId")
    void deleteLoginStatus(@Param("userId") Long userid);


}
