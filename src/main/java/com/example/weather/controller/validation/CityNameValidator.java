package com.example.weather.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class CityNameValidator implements ConstraintValidator<CityNameConstraint, String> {
    @Override
    public void initialize(CityNameConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        // Herf ve reqemden basqa butun simvollar "" ile deyisdirilir
        value = value.replaceAll("[^a-zA-Z0-9]", "");

        // String sadece reqemden ibaretdirse ve bossa false qaytar

        return !StringUtils.isNumeric(value) && !StringUtils.isAllBlank(value);
    }
}
