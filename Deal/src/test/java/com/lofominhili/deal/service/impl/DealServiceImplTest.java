package com.lofominhili.deal.service.impl;

import com.lofominhili.deal.dto.EmploymentDto;
import com.lofominhili.deal.dto.FinishRegistrationRequestDto;
import com.lofominhili.deal.dto.LoanOfferDto;
import com.lofominhili.deal.dto.LoanStatementRequestDto;
import com.lofominhili.deal.entity.Client;
import com.lofominhili.deal.entity.Credit;
import com.lofominhili.deal.entity.Statement;
import com.lofominhili.deal.repository.ClientRepository;
import com.lofominhili.deal.repository.CreditRepository;
import com.lofominhili.deal.repository.StatementRepository;
import com.lofominhili.deal.util.enumeration.ApplicationStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private StatementRepository statementRepository;
    @InjectMocks
    private DealServiceImpl dealService;

    @Test
    void testCreateOffers() {
        final var requestDto = new LoanStatementRequestDto(
                BigDecimal.valueOf(100000),
                7,
                "john",
                "doe",
                "smith",
                "john.doe@example.com",
                LocalDate.of(2000, 1, 2),
                "1234",
                "567890"
        );

        final var mockClient = Client.builder()
                .lastName("Doe")
                .firstName("John")
                .middleName("Middle")
                .build();

        final var mockStatement = Statement.builder()
                .status(ApplicationStatus.PREAPPROVAL)
                .build();

        when(clientRepository.save(any(Client.class))).thenReturn(mockClient);
        when(statementRepository.save(any(Statement.class))).thenReturn(mockStatement);

        final var loanOffers = dealService.createOffers(requestDto);

        assertNotNull(loanOffers);
        assertEquals(4, loanOffers.size());
        verify(clientRepository).save(any(Client.class));
        verify(statementRepository).save(any(Statement.class));
    }

    @Test
    void testUpdateStatementWithOffer() {
        final var loanOffer = new LoanOfferDto(
                UUID.fromString("05e57bee-b673-43e4-8e6e-015c3c7591aa"),
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(200000),
                7,
                BigDecimal.valueOf(29338.43),
                BigDecimal.valueOf(8),
                true,
                true
        );

        final var mockStatement = Statement.builder()
                .id(UUID.randomUUID())
                .status(ApplicationStatus.PREAPPROVAL)
                .build();

        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockStatement));
        when(statementRepository.save(any(Statement.class))).thenReturn(mockStatement);

        dealService.updateStatementWithOffer(loanOffer);

        assertEquals(ApplicationStatus.APPROVED, mockStatement.getStatus());
        assertTrue(mockStatement.getHistory().stream().anyMatch(history -> history.getStatus().equals("APPROVAL")));
        verify(statementRepository).save(any(Statement.class));
    }

    @Test
    void testFinishRegistration() {
        final var registrationRequest = new FinishRegistrationRequestDto(
                "MALE",
                "MARRIED",
                2,
                LocalDate.of(2015, 10, 1),
                "Some branch",
                new EmploymentDto(
                        "EMPLOYED",
                        "1234567890",
                        BigDecimal.valueOf(60000),
                        "MID_MANAGER",
                        2,
                        5
                ),
                "12324123241232412324"
        );

        Statement mockStatement = Statement.builder()
                .id(UUID.fromString("05e57bee-b673-43e4-8e6e-015c3c7591aa"))
                .build();

        Client mockClient = Client.builder()
                .id(UUID.fromString("9ec0ec14-27b1-494a-9bfa-56e23d02db77"))
                .build();


        when(statementRepository.findById(any(UUID.class))).thenReturn(Optional.of(mockStatement));
        when(clientRepository.save(any(Client.class))).thenReturn(mockClient);
        when(creditRepository.save(any(Credit.class))).thenReturn(new Credit());

        dealService.finishRegistration(registrationRequest, mockStatement.getId().toString());

        verify(statementRepository).save(any(Statement.class));
        verify(clientRepository).save(any(Client.class));
        verify(creditRepository).save(any(Credit.class));
    }
}
