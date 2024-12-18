package com.lofominhili.deal.util.validation;

import com.lofominhili.deal.util.validator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValid {
    Class<? extends Enum<?>> enumClass();

    String message() default "Недопустимое значение";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
