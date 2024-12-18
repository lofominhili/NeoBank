package com.lofominhili.deal.entity;

import com.lofominhili.deal.entity.jsonb.Employment;
import com.lofominhili.deal.entity.jsonb.Passport;
import com.lofominhili.deal.util.enumeration.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

import static com.lofominhili.deal.util.Constants.TEXT_MEDIUM_SIZE;
import static com.lofominhili.deal.util.Constants.TEXT_SMALL_SIZE;
import static jakarta.persistence.EnumType.STRING;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SuperBuilder(toBuilder = true)
@Table(name = "client", schema = "db")
@Entity
public class Client extends BaseEntity {

    @Column(name = "last_name", length = TEXT_SMALL_SIZE)
    private String lastName;

    @Column(name = "first_name", length = TEXT_SMALL_SIZE)
    private String firstName;

    @Column(name = "middle_name", length = TEXT_SMALL_SIZE)
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(unique = true, length = TEXT_MEDIUM_SIZE)
    private String email;

    @Enumerated(value = STRING)
    private Gender gender;

    @Column(name = "dependent_amount")
    private Integer dependentAmount;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Passport passport;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Employment employment;

    @Column(name = "account_number")
    private String accountNumber;
}
