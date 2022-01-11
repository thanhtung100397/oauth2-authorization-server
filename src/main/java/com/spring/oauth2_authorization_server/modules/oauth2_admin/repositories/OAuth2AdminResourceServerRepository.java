package com.spring.oauth2_authorization_server.modules.oauth2_admin.repositories;

import com.spring.oauth2_authorization_server.modules.oauth2_admin.models.entities.OAuth2AdminResourceServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuth2AdminResourceServerRepository extends JpaRepository<OAuth2AdminResourceServer, String> {

}
