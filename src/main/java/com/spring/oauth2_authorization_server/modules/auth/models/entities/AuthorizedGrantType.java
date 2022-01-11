package com.spring.oauth2_authorization_server.modules.auth.models.entities;

public enum AuthorizedGrantType {
    CLIENT_CREDENTIALS("client_credentials"),
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token")
    ;

    private String value;

    AuthorizedGrantType(String name) {
        this.value = name;
    }

    @Override
    public String toString() {
        return value;
    }
}
