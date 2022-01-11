package com.spring.oauth2_authorization_server.annotations.validator.collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

public class NoSpaceCollectionValidator implements ConstraintValidator<NoSpaceCollection, Collection<String>> {
    private boolean allowNull;

    @Override
    public void initialize(NoSpaceCollection constraintAnnotation) {
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Collection<String> inputValue, ConstraintValidatorContext context) {
        for (String string : inputValue) {
            if (allowNull) {
                if (string != null && (string.isEmpty() || string.contains(" "))) {
                    return false;
                }
            } else {
                if (string == null || string.isEmpty() || string.contains(" ")) {
                    return false;
                }
            }
        }
        return true;
    }
}
