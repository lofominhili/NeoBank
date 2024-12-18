package com.lofominhili.deal.dto;

import com.lofominhili.deal.util.enumeration.EmploymentPosition;
import com.lofominhili.deal.util.enumeration.EmploymentStatus;
import com.lofominhili.deal.util.validation.EnumValid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import static com.lofominhili.deal.util.Constants.NOT_BLANK_MESSAGE;
import static com.lofominhili.deal.util.Constants.NOT_NULL_MESSAGE;

public record EmploymentDto(

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @EnumValid(enumClass = EmploymentStatus.class, message = "Недопустимое значение статуса занятости")
        String employmentStatus,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Pattern(regexp = "\\d{10}", message = "ИНН работодателя должен содержать 10 цифр")
        String employerINN,

        @NotNull(message = NOT_NULL_MESSAGE)
        @DecimalMin(value = "0.0", inclusive = false, message = "Зарплата должна быть положительным числом")
        BigDecimal salary,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @EnumValid(enumClass = EmploymentPosition.class, message = "Недопустимое значение должности")
        String position,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Min(value = 0, message = "Общий стаж работы не может быть отрицательным")
        Integer workExperienceTotal,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Min(value = 0, message = "Стаж работы на текущем месте не может быть отрицательным")
        Integer workExperienceCurrent
) {
}
