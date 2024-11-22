package com.lofominhili.calculator.dto;

import com.lofominhili.calculator.util.enumeration.Gender;
import com.lofominhili.calculator.util.enumeration.MaritalStatus;
import com.lofominhili.calculator.util.validation.AgeValid;
import com.lofominhili.calculator.util.validation.EnumValid;
import com.lofominhili.calculator.util.validation.ExperienceValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.lofominhili.calculator.util.Constants.*;

public record ScoringDataDto(

        @NotNull(message = NOT_NULL_MESSAGE)
        @DecimalMin(value = "20000", message = "Сумма кредита должна быть не менее 20000")
        BigDecimal amount,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Min(value = 6, message = "Срок кредита должен быть не менее 6 месяцев")
        Integer term,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Pattern(regexp = "^[A-Za-z]{2,30}$", message = "Имя должно содержать от 2 до 30 латинских букв")
        String firstName,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Pattern(regexp = "^[A-Za-z]{2,30}$", message = "Фамилия должна содержать от 2 до 30 латинских букв")
        String lastName,

        @Pattern(regexp = "^[A-Za-z]{2,30}$", message = "Отчество должно содержать от 2 до 30 латинских букв")
        String middleName,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @EnumValid(enumClass = Gender.class, message = "Недопустимое значение пола")
        String gender,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Past(message = PAST_MESSAGE)
        @AgeValid
        LocalDate birthDate,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Pattern(regexp = "\\d{4}", message = "Серия паспорта должна состоять из 4 цифр")
        String passportSeries,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Pattern(regexp = "\\d{6}", message = "Номер паспорта должен состоять из 6 цифр")
        String passportNumber,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Past(message = PAST_MESSAGE)
        LocalDate passportIssueDate,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Size(min = 1, max = 100, message = "Отделение выдачи должно быть от 1 до 100 символов")
        String passportIssueBranch,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @EnumValid(enumClass = MaritalStatus.class, message = "Недопустимое значение семейного положения")
        String maritalStatus,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Min(value = 0, message = "Количество иждивенцев не может быть отрицательным")
        Integer dependentAmount,

        @Valid
        EmploymentDto employmentDto,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Size(min = 20, max = 20, message = "Номер счёта должен содержать 20 символов")
        String accountNumber,

        @NotNull(message = NOT_NULL_MESSAGE)
        Boolean isInsuranceEnabled,

        @NotNull(message = NOT_NULL_MESSAGE)
        Boolean isSalaryClient
) {
}