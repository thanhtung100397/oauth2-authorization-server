package com.spring.oauth2_authorization_server.annotations.validator.text.not_empty;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoEmptyValidator.class)
public @interface NoEmpty {
    String message() default "string must not be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean allowNull() default false;
}
