package com.lofominhili.deal.dto;

import com.lofominhili.deal.util.enumeration.Gender;
import com.lofominhili.deal.util.enumeration.MaritalStatus;
import com.lofominhili.deal.util.validation.EnumValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static com.lofominhili.deal.util.Constants.*;

public record FinishRegistrationRequestDto(

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @EnumValid(enumClass = Gender.class, message = "Недопустимое значение гендера")
        String gender,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @EnumValid(enumClass = MaritalStatus.class, message = "Недопустимое значение семейного положения")
        String maritalStatus,

        @NotNull(message = NOT_BLANK_MESSAGE)
        Integer dependentAmount,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Past(message = PAST_MESSAGE)
        LocalDate passportIssueDate,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Size(min = 1, max = 100, message = "Отделение выдачи должно быть от 1 до 100 символов")
        String passportIssueBranch,

        @Valid
        EmploymentDto employment,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Size(min = 20, max = 20, message = "Номер счёта должен содержать 20 символов")
        String accountNumber
) {
}
