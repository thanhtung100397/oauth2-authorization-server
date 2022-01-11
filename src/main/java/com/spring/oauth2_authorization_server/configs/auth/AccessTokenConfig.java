package com.spring.oauth2_authorization_server.configs.auth;

import com.spring.oauth2_authorization_server.modules.auth.models.dtos.CustomUserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class AccessTokenConfig {
    @Value("${security.oauth2.authorization-server.token-signing-key}")
    private String jwtSigningKey;
    @Value("${security.oauth2.authorization-server.access-token.validity-seconds}")
    private int accessTokenValiditySeconds;
    @Value("${security.oauth2.authorization-server.refresh-token.validity-seconds}")
    private int refreshTokenValiditySeconds;

    @Bean
    TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(jwtSigningKey);
        return jwtAccessTokenConverter;
    }

    @Bean
    @Primary
    DefaultTokenServices tokenServices(TokenStore tokenStore,
                                       TokenEnhancer customTokenEnhancer,
                                       JwtAccessTokenConverter jwtAccessTokenConverter) {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customTokenEnhancer, jwtAccessTokenConverter));

        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore);
        defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);
        return defaultTokenServices;
    }

    @Bean
    TokenEnhancer customTokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> info = new HashMap<>();
            //TODO add custom claims for generated access token here
            try {
                CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getUserAuthentication().getPrincipal();
                info.put("user_id", customUserDetail.getUserID());
            } catch (Exception ignore) {
            }
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            return accessToken;
        };
    }
}
