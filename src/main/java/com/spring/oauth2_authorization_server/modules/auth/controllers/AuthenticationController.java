package com.spring.oauth2_authorization_server.modules.auth.controllers;

import com.spring.oauth2_authorization_server.annotations.swagger.Response;
import com.spring.oauth2_authorization_server.annotations.swagger.Responses;
import com.spring.oauth2_authorization_server.base.controllers.BaseRESTController;
import com.spring.oauth2_authorization_server.base.models.BaseResponse;
import com.spring.oauth2_authorization_server.constants.ResponseValue;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.UsernamePasswordDto;
import com.spring.oauth2_authorization_server.modules.auth.models.dtos.RefreshTokenDto;
import com.spring.oauth2_authorization_server.modules.auth.services.AuthenticationService;
import com.spring.oauth2_authorization_server.swagger.auth.AuthenticationResultSwagger;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("api/authentication/")
public class AuthenticationController extends BaseRESTController {
    @Autowired
    private AuthenticationService authenticationService;

    @ApiOperation(value = "Xác thực bằng người dùng bằng username và password",
            notes = "Xác thực thông qua username và password của người dùng, " +
                    "yêu cầu thêm Basic Auth của ứng dụng thực hiện gọi api",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = AuthenticationResultSwagger.class),
            @Response(responseValue = ResponseValue.WRONG_CLIENT_ID_OR_SECRET),
            @Response(responseValue = ResponseValue.WRONG_USERNAME_OR_PASSWORD)
    })
    @PostMapping("/username-password")
    public BaseResponse authenticateByUsernamePassword(@RequestHeader(value = "client_id") String clientID,
                                                       @RequestHeader(value = "secret") String secret,
                                                       @RequestBody @Valid UsernamePasswordDto usernamePasswordDto) {
        return authenticationService.authenticateByUsernameAndPassword(clientID, secret, usernamePasswordDto);
    }

    @ApiOperation(value = "Xác thực người dùng bằng refresh token",
            notes = "Xác thực thông qua refresh token của người dùng, " +
                    "yêu cầu thêm Basic Auth của ứng dụng thực hiện gọi api",
            response = Iterable.class)
    @Responses(value = {
            @Response(responseValue = ResponseValue.SUCCESS, responseBody = AuthenticationResultSwagger.class),
            @Response(responseValue = ResponseValue.WRONG_CLIENT_ID_OR_SECRET),
            @Response(responseValue = ResponseValue.INVALID_TOKEN),
            @Response(responseValue = ResponseValue.EXPIRED_TOKEN)
    })
    @PostMapping("/refresh-token")
    public BaseResponse authenticateByRefreshToken(@RequestHeader(value = "client_id") String clientID,
                                                   @RequestHeader(value = "secret") String secret,
                                                   @RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return authenticationService.authenticateByRefreshToken(clientID, secret, refreshTokenDto);
    }
}
