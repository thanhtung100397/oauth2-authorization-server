package com.spring.oauth2_authorization_server.modules.auth.services;

import com.spring.oauth2_authorization_server.constants.ResponseValue;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.CustomUserDetail;
import com.spring.oauth2_authorization_server.modules.auth.models.entities.User;
import com.spring.oauth2_authorization_server.modules.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class QueryUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(ResponseValue.WRONG_USERNAME_OR_PASSWORD.message());
        }
        return new CustomUserDetail(user.getId(), user.getUsername(), user.getPassword(),
                Collections.emptyList());
    }
}
