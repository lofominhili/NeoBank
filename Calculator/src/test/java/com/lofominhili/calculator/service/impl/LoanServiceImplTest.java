package com.lofominhili.calculator.service.impl;

import com.lofominhili.calculator.configuration.TestProperties;
import com.lofominhili.calculator.dto.EmploymentDto;
import com.lofominhili.calculator.dto.LoanStatementRequestDto;
import com.lofominhili.calculator.dto.ScoringDataDto;
import com.lofominhili.calculator.util.exception.CalculationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoanServiceImplTest {

    private final LoanServiceImpl loanService;
    private final TestProperties testProperties;

    @Autowired
    LoanServiceImplTest(LoanServiceImpl loanService, TestProperties testProperties) {
        this.loanService = loanService;
        this.testProperties = testProperties;
    }

    @Test
    void makeOffers_shouldReturnValidLoanOffers() {
        var request = new LoanStatementRequestDto(
                BigDecimal.valueOf(100_000),
                12,
                "John",
                "Doe",
                "Smith",
                "john.doe@example.com",
                LocalDate.of(1990, 5, 20),
                "1234",
                "567890"
        );

        var offers = loanService.makeOffers(request);

        assertNotNull(offers);
        assertEquals(4, offers.size());

        offers.forEach(offer -> {
            assertNotNull(offer.statementId());
            assertEquals(request.amount(), offer.requestedAmount());
            assertTrue(offer.rate().compareTo(testProperties.baseRate()) <= 0);
            assertTrue(offer.totalAmount().compareTo(request.amount()) >= 0);
            assertTrue(offer.monthlyPayment().compareTo(BigDecimal.ZERO) > 0);
        });

        for (int i = 1; i < offers.size(); i++) {
            assertTrue(offers.get(i - 1).rate().compareTo(offers.get(i).rate()) <= 0);
        }
    }

    @Test
    void calculateCredit_shouldReturnValidCreditDto() {
        var scoringData = new ScoringDataDto(
                BigDecimal.valueOf(100_000),
                12,
                "John",
                "Doe",
                "Smith",
                "male",
                LocalDate.of(1985, 3, 15),
                "1234",
                "567890",
                LocalDate.of(2015, 10, 1),
                "Some Branch",
                "married",
                2,
                new EmploymentDto(
                        "self_employed",
                        "1234567890",
                        BigDecimal.valueOf(60_000),
                        "middle",
                        10,
                        5
                ),
                "12345678901234567890",
                true,
                true
        );

        var creditDto = loanService.calculateCredit(scoringData);

        assertNotNull(creditDto);
        assertEquals(scoringData.amount(), creditDto.amount());
        assertEquals(scoringData.term(), creditDto.term());
        assertTrue(creditDto.monthlyPayment().compareTo(BigDecimal.ZERO) > 0);
        assertNotNull(creditDto.paymentSchedule());
        assertEquals(scoringData.isInsuranceEnabled(), creditDto.isInsuranceEnabled());
        assertEquals(scoringData.isSalaryClient(), creditDto.isSalaryClient());
        assertEquals(creditDto.paymentSchedule().size(), (int) scoringData.term());
    }

    @Test
    void calculateCredit_shouldHandleInvalidData() {
        var scoringData = new ScoringDataDto(
                BigDecimal.valueOf(10_000),
                3,
                "John",
                "Doe",
                "Smith",
                "male",
                LocalDate.of(2010, 1, 1),
                "1234",
                "567890",
                LocalDate.of(2023, 10, 1),
                "Some Branch",
                "single",
                -1,
                new EmploymentDto(
                        "unemployed",
                        "12345",
                        BigDecimal.valueOf(15_000),
                        "junior",
                        -5,
                        -2
                ),
                "12345678901234567890",
                false,
                false
        );

        assertThrows(CalculationException.class, () -> loanService.calculateCredit(scoringData));
    }

    @Test
    void calculateCredit_shouldGenerateValidSchedule() {
        var scoringData = new ScoringDataDto(
                BigDecimal.valueOf(105_000),
                12,
                "John",
                "Doe",
                "Smith",
                "male",
                LocalDate.of(1985, 3, 15),
                "1234",
                "567890",
                LocalDate.of(2015, 10, 1),
                "Some Branch",
                "married",
                0,
                new EmploymentDto(
                        "self_employed",
                        "1234567890",
                        BigDecimal.valueOf(60_000),
                        "middle",
                        10,
                        5
                ),
                "12345678901234567890",
                true,
                true
        );

        var creditDto = loanService.calculateCredit(scoringData);

        assertNotNull(creditDto.paymentSchedule());
        assertEquals(scoringData.term(), creditDto.paymentSchedule().size());
        var remainingDebt = scoringData.amount().add(testProperties.insuranceCost());
        for (var element : creditDto.paymentSchedule()) {
            assertNotNull(element.date());
            assertTrue(element.totalPayment().compareTo(BigDecimal.ZERO) > 0);
            remainingDebt = element.remainingDebt();
            assertTrue(remainingDebt.compareTo(BigDecimal.ZERO) >= 0);
        }
    }

}