package com.lofominhili.calculator.service;

import com.lofominhili.calculator.dto.ScoringDataDto;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public interface ScoringService {

    @NotNull
    BigDecimal calculateAdjustedRate(@NotNull final ScoringDataDto scoringData, BigDecimal baseRate);
}
