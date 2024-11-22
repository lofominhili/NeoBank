package com.lofominhili.calculator.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class AgeValidator implements ConstraintValidator<AgeValid, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {
        if (birthDate == null) {
            return true;
        }
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        return age >= 20 && age <= 65;
    }
}
