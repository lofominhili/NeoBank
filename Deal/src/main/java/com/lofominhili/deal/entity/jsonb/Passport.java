package com.lofominhili.deal.entity.jsonb;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
public class Passport {

    private UUID id;

    private String series;

    private String number;

    private String issueBranch;

    private LocalDate issueDate;
}
