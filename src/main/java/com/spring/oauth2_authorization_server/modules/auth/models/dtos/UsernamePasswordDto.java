package com.spring.oauth2_authorization_server.modules.auth.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class UsernamePasswordDto {
    @ApiModelProperty(notes = "username of user account", example = "NOT_NULL")
    @JsonProperty("username")
    @NotNull
    private String username;
    @ApiModelProperty(notes = "password of user account", example = "NOT_NULL", position = 1)
    @JsonProperty("password")
    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
