package com.lofominhili.deal.entity;

import com.lofominhili.deal.util.enumeration.CreditStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
@Table(name = "credit", schema = "db")
@Entity
public class Credit extends BaseEntity {

    private BigDecimal amount;

    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private BigDecimal psk;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_Schedule", columnDefinition = "jsonb")
    private String paymentSchedule;

    @Column(name = "insurance_Enabled")
    private Boolean insuranceEnabled;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Enumerated(value = STRING)
    private CreditStatus status;
}
