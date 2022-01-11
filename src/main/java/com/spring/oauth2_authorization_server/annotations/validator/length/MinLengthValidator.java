package com.spring.oauth2_authorization_server.annotations.validator.length;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinLengthValidator implements ConstraintValidator<MinLength, String> {
    private int minCharacter;

    @Override
    public void initialize(MinLength constraintAnnotation) {
        this.minCharacter = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext context) {
        return inputValue == null || inputValue.length() >= minCharacter;
    }
}
