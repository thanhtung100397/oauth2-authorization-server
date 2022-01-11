package com.spring.oauth2_authorization_server.modules.auth.repositories;

import com.spring.oauth2_authorization_server.modules.auth.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findFirstByUsername(String username);

    @Query("select u.isBanned from User u where u.id = ?1")
    boolean isUserBanned(String userID);

    @Modifying
    @Transactional
    @Query("update User u set u.lastActive = ?2 where u.id = ?1")
    void updateLastActive(String userID, Date lastActive);
}
