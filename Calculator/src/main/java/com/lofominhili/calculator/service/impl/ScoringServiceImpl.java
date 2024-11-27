package com.lofominhili.calculator.service.impl;

import com.lofominhili.calculator.dto.ScoringDataDto;
import com.lofominhili.calculator.service.ScoringService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Stream;

import static com.lofominhili.calculator.util.exception.CalculationException.Code.CREDIT_ACCESS_DENIED;

@Slf4j
@Service
public class ScoringServiceImpl implements ScoringService {

    @Override
    @NotNull
    public BigDecimal calculateAdjustedRate(@NotNull final ScoringDataDto scoringData, @NotNull final BigDecimal baseRate) {
        log.info("Начало расчета скоринговой ставки для данных: {}, базовая ставка: {}", scoringData, baseRate);
        applySalaryExperienceRule(scoringData);

        BigDecimal totalAdjustment = Stream.of(
                        applyEmploymentStatusRule(scoringData, baseRate),
                        applyJobPositionRule(scoringData, baseRate),
                        applyMaritalStatusRule(scoringData, baseRate),
                        applyGenderRule(scoringData, baseRate)
                )
                .map(result -> result.subtract(baseRate))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal adjustedRate = baseRate.add(totalAdjustment);

        log.info("Завершен расчет скоринговой ставки: итоговая ставка: {}", adjustedRate);
        return adjustedRate;
    }

    private void applySalaryExperienceRule(@NotNull final ScoringDataDto scoringData) {
        final var amount = scoringData.amount();
        final var salary = scoringData.employmentDto().salary();
        final var workExperienceTotal = scoringData.employmentDto().workExperienceTotal();
        final var workExperienceCurrent = scoringData.employmentDto().workExperienceCurrent();
        if (amount.compareTo(salary.multiply(BigDecimal.valueOf(24))) > 0) {
            log.error("Сумма кредита превышает 24 зарплаты — отказ в кредите");
            throw CREDIT_ACCESS_DENIED.get("Сумма кредита превышает 24 зарплаты — отказ в кредите");
        } else if (workExperienceTotal < 18 && workExperienceCurrent < 3) {
            log.error("Недостаточный стаж работы — отказ в кредите");
            throw CREDIT_ACCESS_DENIED.get("Недостаточный стаж работы — отказ в кредите");
        }
    }

    private BigDecimal applyEmploymentStatusRule(@NotNull final ScoringDataDto scoringData, @NotNull final BigDecimal rate) {
        final var employmentStatus = scoringData.employmentDto().employmentStatus().toUpperCase();
        log.debug("Применение правила по статусу занятости: {}, текущая ставка: {}", employmentStatus, rate);

        return switch (employmentStatus) {
            case "UNEMPLOYED" -> {
                log.error("Безработный клиент — отказ в кредите");
                throw CREDIT_ACCESS_DENIED.get("Безработный — отказ в кредите");
            }
            case "SELF_EMPLOYED" -> {
                log.debug("Самозанятый — ставка увеличена на 2");
                yield rate.add(BigDecimal.valueOf(2));
            }
            case "BUSINESS_OWNER" -> {
                log.debug("Владелец бизнеса — ставка увеличена на 1");
                yield rate.add(BigDecimal.valueOf(1));
            }
            default -> rate;
        };
    }

    private BigDecimal applyJobPositionRule(@NotNull final ScoringDataDto scoringData, @NotNull final BigDecimal rate) {
        final var position = scoringData.employmentDto().position().toUpperCase();
        log.debug("Применение правила по позиции на работе: {}, текущая ставка: {}", position, rate);

        return switch (position) {
            case "MIDDLE" -> {
                log.debug("Менеджер среднего звена — ставка уменьшена на 2");
                yield rate.subtract(BigDecimal.valueOf(2));
            }
            case "SENIOR" -> {
                log.debug("Топ-менеджер — ставка уменьшена на 3");
                yield rate.subtract(BigDecimal.valueOf(3));
            }
            default -> rate;
        };
    }

    private BigDecimal applyMaritalStatusRule(@NotNull final ScoringDataDto scoringData, @NotNull final BigDecimal rate) {
        final var maritalStatus = scoringData.maritalStatus().toUpperCase();
        log.debug("Применение правила по семейному положению: {}, текущая ставка: {}", maritalStatus, rate);

        return switch (maritalStatus) {
            case "MARRIED" -> {
                log.debug("Женат/Замужем — ставка уменьшена на 3");
                yield rate.subtract(BigDecimal.valueOf(3));
            }
            case "DIVORCED" -> {
                log.debug("Разведен — ставка увеличена на 1");
                yield rate.add(BigDecimal.valueOf(1));
            }
            default -> rate;
        };
    }

    private BigDecimal applyGenderRule(@NotNull final ScoringDataDto scoringData, @NotNull final BigDecimal rate) {
        final var gender = scoringData.gender().toUpperCase();
        final var age = Period.between(scoringData.birthDate(), LocalDate.now()).getYears();
        log.debug("Применение правила по гендеру: {}, возраст: {}, текущая ставка: {}", gender, age, rate);

        return switch (gender) {
            case "FEMALE" -> {
                if (age >= 32 && age <= 60) {
                    log.debug("Женщина в подходящем возрасте — ставка уменьшена на 3");
                    yield rate.subtract(BigDecimal.valueOf(3));
                }
                yield rate;
            }
            case "MALE" -> {
                if (age >= 30 && age <= 55) {
                    log.debug("Мужчина в подходящем возрасте — ставка уменьшена на 3");
                    yield rate.subtract(BigDecimal.valueOf(3));
                }
                yield rate;
            }
            case "NON_BINARY" -> {
                log.debug("Не бинарный — ставка увеличена на 7");
                yield rate.add(BigDecimal.valueOf(7));
            }
            default -> rate;
        };
    }


}


