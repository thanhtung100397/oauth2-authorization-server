package com.spring.oauth2_authorization_server.exceptions;

import com.spring.oauth2_authorization_server.constants.ResponseValue;

public class AuthorizationException extends Exception {
    private ResponseValue responseValue;

    public AuthorizationException(ResponseValue responseValue) {
        super(responseValue.message());
        this.responseValue = responseValue;
    }

    public ResponseValue getResponseValue() {
        return responseValue;
    }
}
