package com.lofominhili.calculator.util.validation;

import com.lofominhili.calculator.util.validator.AgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AgeValid {

    String message() default "Возраст должен быть не менее 18 и не более 65 лет";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
