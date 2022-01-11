package com.spring.oauth2_authorization_server.modules.auth.services;

import com.spring.oauth2_authorization_server.exceptions.AuthorizationException;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.AuthorizedUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface IAuthorization {
    void authorizeUser(String method, String route, AuthorizedUser authorizedUser,
                       Collection<? extends GrantedAuthority> authorities) throws AuthorizationException;
}
