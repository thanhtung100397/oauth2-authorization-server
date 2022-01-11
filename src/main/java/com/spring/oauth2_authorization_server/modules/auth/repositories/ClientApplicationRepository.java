package com.spring.oauth2_authorization_server.modules.auth.repositories;

import com.spring.oauth2_authorization_server.modules.auth.models.entities.ClientApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ClientApplicationRepository extends JpaRepository<ClientApplication, String> {
    @Query("select c from ClientApplication c " +
            "left join fetch c.resourceServers " +
            "where c.clientID = ?1")
    ClientApplication getClientApplicationWithFetchedResourceServer(String clientID);

    @Query("select c " +
            "from ClientApplication c " +
            "left join fetch c.resourceServers " +
            "where c.clientID in ?1")
    Set<ClientApplication> getAllFetchedClientApplicationIn(Set<String> clientIDs);

    boolean existsByClientID(String clientID);
}
