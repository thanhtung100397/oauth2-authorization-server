package com.spring.oauth2_authorization_server.configs.auth;

import com.spring.oauth2_authorization_server.exceptions.CustomOAuth2Exception;
import com.spring.oauth2_authorization_server.modules.auth.services.QueryClientApplicationDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private WebResponseExceptionTranslator authExceptionTranslator;
    @Autowired
    private QueryClientApplicationDetailsService clientDetailsService;

    @Bean
    public WebResponseExceptionTranslator authExceptionTranslator() {
        return e -> {
            if (e instanceof OAuth2Exception) {
                CustomOAuth2Exception customOAuth2Exception = new CustomOAuth2Exception((OAuth2Exception) e);
                return ResponseEntity
                        .status(customOAuth2Exception.getResponseValue().httpStatus())
                        .body(customOAuth2Exception);
            } else {
                throw e;
            }
        };
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenServices(tokenServices)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(authExceptionTranslator);
    }
}
