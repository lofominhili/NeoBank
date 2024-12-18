package com.lofominhili.calculator.util.validator;

import com.lofominhili.calculator.dto.ScoringDataDto;
import com.lofominhili.calculator.util.validation.ExperienceValid;
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
