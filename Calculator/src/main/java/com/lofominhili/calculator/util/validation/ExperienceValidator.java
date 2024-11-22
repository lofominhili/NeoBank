package com.lofominhili.calculator.util.validation;

import com.lofominhili.calculator.dto.ScoringDataDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ExperienceValidator implements ConstraintValidator<ExperienceValid, ScoringDataDto> {

    @Override
    public boolean isValid(ScoringDataDto value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.employmentDto().workExperienceTotal() >= 18 && value.employmentDto().workExperienceCurrent() >= 3;
    }
}
