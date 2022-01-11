package com.spring.oauth2_authorization_server.modules.auth.repositories;

import com.spring.oauth2_authorization_server.modules.auth.models.entities.ResourceServer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface ResourceServerRepository extends JpaRepository<ResourceServer, String> {
    Set<ResourceServer> findAllByResourceIDIn(Set<String> resourceIDs);

    @Query("select r.resourceID from ResourceServer r")
    Set<String> getAllResourceServerID(Sort sort);

    @Query("select r.resourceID from ResourceServer r where r.resourceID in ?1")
    Set<String> getAllResourceServerIDWithIDIn(Set<String> resourceIDs);

    @Modifying
    @Transactional
    void deleteAllByResourceIDIn(Set<String> resourceIDs);
}
