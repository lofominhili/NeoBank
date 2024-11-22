package com.lofominhili.calculator.service.impl;

import com.lofominhili.calculator.dto.*;
import com.lofominhili.calculator.service.LoanService;
import com.lofominhili.calculator.service.ScoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    @Value("${loan.base-rate}")
    private BigDecimal baseRate;

    @Value("${loan.insurance-discount}")
    private BigDecimal insuranceDiscount;

    @Value("${loan.salary-client-discount}")
    private BigDecimal salaryClientDiscount;

    @Value("${loan.insurance-cost}")
    private BigDecimal insuranceCost;

    private final ScoringService scoringService;


    @Override
    @NotNull
    public List<LoanOfferDto> makeOffers(@NotNull final LoanStatementRequestDto request) {
        log.info("Начало создания предложений для суммы: {} и срока: {}", request.amount(), request.term());

        final var offers = Stream.of(false, true)
                .flatMap(isInsuranceEnabled -> Stream.of(false, true)
                        .map(isSalaryClient -> {
                            final var rate = calculateRate(isInsuranceEnabled, isSalaryClient, baseRate);
                            final var totalAmount = calculateTotalAmount(request.amount(), isInsuranceEnabled);
                            final var monthlyPayment = calculateMonthlyPayment(totalAmount, rate, request.term());

                            log.debug("Создание предложения: ставка: {}, сумма: {}, платеж: {}", rate, totalAmount, monthlyPayment);

                            return new LoanOfferDto(
                                    UUID.randomUUID(),
                                    request.amount(),
                                    totalAmount,
                                    request.term(),
                                    monthlyPayment,
                                    rate,
                                    isInsuranceEnabled,
                                    isSalaryClient
                            );
                        }))
                .sorted(Comparator.comparing(LoanOfferDto::rate))
                .toList();

        log.info("Завершено создание предложений. Количество: {}", offers.size());
        return offers;
    }

    @Override
    @NotNull
    public CreditDto calculateCredit(@NotNull final ScoringDataDto scoringData) {
        log.info("Начало расчета кредита для суммы: {} и срока: {}", scoringData.amount(), scoringData.term());

        final var adjustedRate = scoringService.calculateAdjustedRate(scoringData, baseRate);
        final var rate = calculateRate(scoringData.isInsuranceEnabled(), scoringData.isSalaryClient(), adjustedRate);
        final var totalAmount = calculateTotalAmount(scoringData.amount(), scoringData.isInsuranceEnabled());
        final var monthlyPayment = calculateMonthlyPayment(totalAmount, rate, scoringData.term());

        final var paymentSchedule = IntStream.rangeClosed(1, scoringData.term())
                .mapToObj(i -> generatePaymentScheduleElement(i, totalAmount, rate, monthlyPayment, scoringData.term()))
                .toList();

        final var psk = calculatePSK(paymentSchedule, scoringData.amount());
        log.debug("Расчет завершен: ставка: {}, ежемесячный платеж: {}, ПСК: {}", rate, monthlyPayment, psk);

        return new CreditDto(
                scoringData.amount(),
                scoringData.term(),
                monthlyPayment,
                rate,
                psk,
                scoringData.isInsuranceEnabled(),
                scoringData.isSalaryClient(),
                paymentSchedule
        );
    }

    private PaymentScheduleElementDto generatePaymentScheduleElement(final int month, @NotNull final BigDecimal totalAmount, @NotNull final BigDecimal rate, @NotNull final BigDecimal monthlyPayment, int term) {
        final var monthlyRate = rate.divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP);
        var remainingDebt = totalAmount.subtract(monthlyPayment.multiply(BigDecimal.valueOf(month - 1L)));
        final var interestPayment = remainingDebt.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
        final var debtPayment = monthlyPayment.subtract(interestPayment).setScale(2, RoundingMode.HALF_UP);
        remainingDebt = remainingDebt.subtract(debtPayment).max(BigDecimal.ZERO);
        final var paymentDate = LocalDate.now().plusMonths(month);

        log.trace("Генерация элемента графика для месяца {}: платеж: {}, процент: {}, долг: {}", month, monthlyPayment, interestPayment, debtPayment);
        return new PaymentScheduleElementDto(month, paymentDate, monthlyPayment, interestPayment, debtPayment, remainingDebt);
    }

    private BigDecimal calculatePSK(@NotNull final List<PaymentScheduleElementDto> paymentSchedule, @NotNull final BigDecimal loanAmount) {
        final var totalPayments = paymentSchedule.stream()
                .map(PaymentScheduleElementDto::totalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final var psk = totalPayments.subtract(loanAmount)
                .divide(loanAmount, 4, RoundingMode.HALF_UP);

        log.debug("PSK рассчитан: {}", psk);
        return psk;
    }

    private BigDecimal calculateRate(final boolean isInsuranceEnabled, final boolean isSalaryClient, @NotNull final BigDecimal initialRate) {
        final var rate = initialRate
                .subtract(isInsuranceEnabled ? insuranceDiscount : BigDecimal.ZERO)
                .subtract(isSalaryClient ? salaryClientDiscount : BigDecimal.ZERO);

        log.trace("Расчет ставки: страховка: {}, зарплатный клиент: {}, ставка: {}", isInsuranceEnabled, isSalaryClient, rate);
        return rate;
    }

    private BigDecimal calculateTotalAmount(@NotNull final BigDecimal amount, final boolean isInsuranceEnabled) {
        final var totalAmount = isInsuranceEnabled ? amount.add(insuranceCost) : amount;

        log.trace("Общая сумма кредита с учетом страховки: {}", totalAmount);
        return totalAmount;
    }

    private BigDecimal calculateMonthlyPayment(@NotNull final BigDecimal totalAmount, @NotNull final BigDecimal annualRate, final int termInMonths) {
        final var monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 6, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        final var numerator = monthlyRate.multiply(totalAmount);
        final var denominator = BigDecimal.ONE.subtract((BigDecimal.ONE.add(monthlyRate))
                .pow(-termInMonths, new MathContext(12, RoundingMode.HALF_UP)));

        final var monthlyPayment = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        log.trace("Ежемесячный платеж рассчитан: {}", monthlyPayment);
        return monthlyPayment;
    }
}
