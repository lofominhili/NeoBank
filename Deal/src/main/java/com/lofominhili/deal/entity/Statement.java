package com.lofominhili.deal.entity;

import com.lofominhili.deal.entity.jsonb.StatusHistory;
import com.lofominhili.deal.util.enumeration.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
@Table(name = "statement", schema = "db")
@Entity
public class Statement extends BaseEntity {

    @ManyToOne
    private Client client;

    @ManyToOne
    private Credit credit;

    @Enumerated(value = STRING)
    private ApplicationStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer", columnDefinition = "jsonb")
    private String appliedOffer;

    @Column(name = "sign_date")
    private Instant signDate;

    @Column(name = "sec_code")
    private String secCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history", columnDefinition = "jsonb")
    private List<StatusHistory> history;
}
