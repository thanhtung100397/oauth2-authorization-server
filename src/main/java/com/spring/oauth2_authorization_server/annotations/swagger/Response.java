package com.spring.oauth2_authorization_server.annotations.swagger;

import com.spring.oauth2_authorization_server.constants.ResponseValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Response {
    ResponseValue responseValue();
    Class<?> responseBody() default Void.class;
}
