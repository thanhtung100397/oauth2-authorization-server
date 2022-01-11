package com.spring.oauth2_authorization_server.modules.auth.services;

import com.google.common.collect.Lists;
import com.spring.oauth2_authorization_server.modules.auth.models.entities.ClientApplication;
import com.spring.oauth2_authorization_server.modules.auth.models.entities.ResourceServer;
import com.spring.oauth2_authorization_server.modules.auth.repositories.ClientApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueryClientApplicationDetailsService implements ClientDetailsService {
    @Autowired
    private ClientApplicationRepository clientApplicationRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        ClientApplication clientApplication = clientApplicationRepository.getClientApplicationWithFetchedResourceServer(clientId);
        if (clientApplication == null) {
            throw new ClientRegistrationException("client application not found");
        }
        BaseClientDetails result = new BaseClientDetails();
        result.setClientId(clientApplication.getClientID());
        result.setClientSecret(clientApplication.getSecret());
        Set<String> resourceIDs = new HashSet<>();
        for (ResourceServer resourceServer : clientApplication.getResourceServers()) {
            resourceIDs.add(resourceServer.getResourceID());
        }
        result.setResourceIds(resourceIDs);
        result.setScope(Lists.newArrayList("read", "write"));
        result.setAuthorizedGrantTypes(clientApplication.getAuthorizedGrantTypes());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_TRUSTED_CLIENT"));
        result.setAuthorities(authorities);
        return result;
    }
}
