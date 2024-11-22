package com.lofominhili.calculator.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExperienceValidator.class)
@Documented
public @interface ExperienceValid {

    String message() default "Недостаточный стаж работы";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
