package com.lofominhili.deal.dto;

import com.lofominhili.deal.util.validation.AgeValid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.lofominhili.deal.util.Constants.*;

public record LoanStatementRequestDto(

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
        @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Некорректный формат email")
        String email,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Past(message = PAST_MESSAGE)
        @AgeValid
        LocalDate birthDate,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна содержать 4 цифры")
        String passportSeries,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен содержать 6 цифр")
        String passportNumber
) {
}