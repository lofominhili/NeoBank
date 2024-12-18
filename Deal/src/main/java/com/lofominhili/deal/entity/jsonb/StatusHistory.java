package com.lofominhili.deal.entity.jsonb;

import com.lofominhili.deal.util.enumeration.ChangeType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
public class StatusHistory {

    private String status;

    private Instant time;

    private ChangeType changeType;
}
