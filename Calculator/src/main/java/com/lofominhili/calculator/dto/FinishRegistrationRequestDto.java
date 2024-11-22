package com.lofominhili.calculator.dto;

import com.lofominhili.calculator.util.enumeration.Gender;
import com.lofominhili.calculator.util.enumeration.MaritalStatus;

import java.time.LocalDate;

public record FinishRegistrationRequestDto(

        Gender gender,

        MaritalStatus maritalStatus,

        Integer dependentAmount,

        LocalDate passportIssueDate,

        String passportIssueBranch,

        EmploymentDto employment,

        String accountNumber
) {
}
