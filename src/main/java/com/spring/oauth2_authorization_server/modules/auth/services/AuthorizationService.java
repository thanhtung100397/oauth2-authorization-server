package com.spring.oauth2_authorization_server.modules.auth.services;

import com.spring.oauth2_authorization_server.constants.ResponseValue;
import com.spring.oauth2_authorization_server.exceptions.AuthorizationException;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.AuthorizedUser;
import com.spring.oauth2_authorization_server.modules.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class AuthorizationService implements IAuthorization {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void authorizeUser(String method, String route,
                              AuthorizedUser authorizedUser, Collection<? extends GrantedAuthority> authorities)
            throws AuthorizationException {
        boolean authorizationResult = userRepository.isUserBanned(authorizedUser.getUserID());
        if (authorizationResult) {
            throw new AuthorizationException(ResponseValue.USER_BANNED);
        }
    }
}
