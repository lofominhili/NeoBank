package com.lofominhili.deal.entity.jsonb;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
public class Employment {

    private UUID id;

    private String status;

    private String employerInn;

    private BigDecimal salary;

    private String position;

    private Integer workExperienceTotal;

    private Integer workExperienceCurrent;
}
