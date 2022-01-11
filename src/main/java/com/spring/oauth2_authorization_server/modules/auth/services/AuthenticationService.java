package com.spring.oauth2_authorization_server.modules.auth.services;

import com.spring.oauth2_authorization_server.base.models.BaseResponse;
import com.spring.oauth2_authorization_server.base.models.BaseResponseBody;
import com.spring.oauth2_authorization_server.constants.ResponseValue;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.AuthenticationResult;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.OriginAuthenticationResult;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.RefreshTokenDto;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.UsernamePasswordDto;
import com.spring.oauth2_authorization_server.modules.auth.repositories.UserRepository;
import com.spring.oauth2_authorization_server.utils.core.Base64Utils;
import com.spring.oauth2_authorization_server.utils.core.JacksonObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Set;

@Service
public class AuthenticationService {
    @Value("${server.port}")
    private int serverPort;
    @Value("${security.jwt.access-token-validity-in-seconds:0}")
    private long accessTokenExpirationTime;
    @Value("${security.jwt.refresh-token-validity-in-seconds:0}")
    private long refreshTokenExpirationTime;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;

    public BaseResponse authenticateByUsernameAndPassword(String clientID, String secret,
                                                          UsernamePasswordDto usernamePasswordDto) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "password");
        requestBody.add("username", usernamePasswordDto.getUsername());
        requestBody.add("password", usernamePasswordDto.getPassword());
        return executeAuthenticationRequest(clientID, secret, requestBody);
    }

    public BaseResponse authenticateByRefreshToken(String clientID, String secret,
                                                   RefreshTokenDto refreshTokenDto) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", refreshTokenDto.getRefreshToken());
        return executeAuthenticationRequest(clientID, secret, requestBody);
    }

    private BaseResponse executeAuthenticationRequest(String clientID, String secret,
                                                      LinkedMultiValueMap<String, String> requestBody) {
        HttpHeaders requestHeader = new HttpHeaders();
        requestHeader.add("Authorization", "Basic " + Base64Utils.encode(clientID + ":" + secret));
        try {
            OriginAuthenticationResult originAuthResult = restTemplate
                    .postForObject("http://localhost:" + serverPort + "/oauth/token",
                            new HttpEntity<>(requestBody, requestHeader),
                            OriginAuthenticationResult.class);
            AuthenticationResult result = new AuthenticationResult(originAuthResult,
                    accessTokenExpirationTime, refreshTokenExpirationTime);
            userRepository.updateLastActive(originAuthResult.getUserID(), new Date());
            return new BaseResponse<>(ResponseValue.SUCCESS, result);
        } catch (HttpStatusCodeException e) {
            try {
                BaseResponseBody<?> responseBody = JacksonObjectMapper.getInstance()
                        .readValue(e.getResponseBodyAsByteArray(), BaseResponseBody.class);
                if (responseBody.getCode() == ResponseValue.AUTHENTICATION_REQUIRED.specialCode()) {
                    return new BaseResponse(ResponseValue.WRONG_CLIENT_ID_OR_SECRET);
                }
                return new BaseResponse<>(e.getStatusCode(), responseBody);
            } catch (Exception mappingException) {
                throw e;
            }
        }
    }
}
