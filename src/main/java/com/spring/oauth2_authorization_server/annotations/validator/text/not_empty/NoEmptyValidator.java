package com.spring.oauth2_authorization_server.annotations.validator.text.not_empty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoEmptyValidator implements ConstraintValidator<NoEmpty, String> {
    private boolean allowNull;

    @Override
    public void initialize(NoEmpty constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String inputValue, ConstraintValidatorContext context) {
        if (allowNull) {
            return inputValue == null || !inputValue.equals("");
        }
        return inputValue != null && !inputValue.equals("");
    }
}
