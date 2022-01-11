package com.spring.oauth2_authorization_server.annotations.validator.collection;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoSpaceCollectionValidator.class)
public @interface NoSpaceCollection {
    String message() default "collection must not contain space character string";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;
}
