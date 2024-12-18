package com.lofominhili.deal.service.impl;

import com.lofominhili.deal.dto.*;
import com.lofominhili.deal.dto.basic.SuccessDto;
import com.lofominhili.deal.entity.Client;
import com.lofominhili.deal.entity.Credit;
import com.lofominhili.deal.entity.Statement;
import com.lofominhili.deal.entity.jsonb.Employment;
import com.lofominhili.deal.entity.jsonb.Passport;
import com.lofominhili.deal.entity.jsonb.StatusHistory;
import com.lofominhili.deal.repository.ClientRepository;
import com.lofominhili.deal.repository.CreditRepository;
import com.lofominhili.deal.repository.StatementRepository;
import com.lofominhili.deal.service.DealService;
import com.lofominhili.deal.util.JsonUtil;
import com.lofominhili.deal.util.enumeration.ApplicationStatus;
import com.lofominhili.deal.util.enumeration.ChangeType;
import com.lofominhili.deal.util.enumeration.CreditStatus;
import com.lofominhili.deal.util.enumeration.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lofominhili.deal.util.exception.NotFoundException.Code.STATEMENT_NOT_FOUND;
import static com.lofominhili.deal.util.exception.ServiceException.Code.CALCULATOR_SERVICE_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final StatementRepository statementRepository;
    private final RestTemplate calculatorRestTemplate;

    @Override
    @NotNull
    public List<LoanOfferDto> createOffers(@NotNull final LoanStatementRequestDto loanStatementRequest) {
        log.info("Received request to create offers: {}", loanStatementRequest);

        final var client = Client.builder()
                .lastName(loanStatementRequest.lastName())
                .firstName(loanStatementRequest.firstName())
                .middleName(loanStatementRequest.middleName())
                .birthDate(loanStatementRequest.birthDate())
                .email(loanStatementRequest.email())
                .passport(new Passport(
                        UUID.randomUUID(),
                        loanStatementRequest.passportSeries(),
                        loanStatementRequest.passportNumber(),
                        null,
                        null))
                .build();
        clientRepository.save(client);
        log.info("Saved new client: {}", client);

        final var statement = Statement.builder()
                .client(client)
                .status(ApplicationStatus.PREAPPROVAL)
                .history(new ArrayList<>(List.of(new StatusHistory("PREAPPROVAL", Instant.now(), ChangeType.AUTOMATIC))))
                .build();
        statementRepository.save(statement);
        log.info("Created and saved new statement: {}", statement);

        log.info("Fetching loan offers for statement: {}", statement.getId());
        var loanOffers = fetchFromCalculatorService(
                "/api/calculator/offers",
                loanStatementRequest,
                new ParameterizedTypeReference<SuccessDto<List<LoanOfferDto>>>() {
                },
                "Failed to fetch loan offers from Calculator service"
        );
        log.info("Received loan offers: {}", loanOffers);

        var enrichedOffers = loanOffers.stream()
                .map(offer -> offer.toBuilder().statementId(statement.getId()).build())
                .collect(Collectors.toList());
        log.info("Enriched loan offers with statement ID: {}", enrichedOffers);

        return enrichedOffers;
    }

    @Override
    public void updateStatementWithOffer(@NotNull final LoanOfferDto loanOffer) {
        log.info("Updating statement with offer: {}", loanOffer);

        var statement = statementRepository.findById(loanOffer.statementId())
                .orElseThrow(() -> STATEMENT_NOT_FOUND.get("Statement not found for id: " + loanOffer.statementId()));
        log.info("Found statement for update: {}", statement);

        statement.setStatus(ApplicationStatus.APPROVED);
        statement.getHistory().add(new StatusHistory("APPROVAL", Instant.now(), ChangeType.AUTOMATIC));
        statement.setAppliedOffer(JsonUtil.toJson(loanOffer));
        statementRepository.save(statement);

        log.info("Updated statement successfully: {}", statement);
    }

    @Override
    public void finishRegistration(@NotNull final FinishRegistrationRequestDto finishRegistrationRequest, @NotNull final String statementId) {
        log.info("Finishing registration for statementId: {}", statementId);

        final var statement = statementRepository.findById(UUID.fromString(statementId))
                .orElseThrow(() -> STATEMENT_NOT_FOUND.get("Statement not found for id: " + statementId));
        log.info("Found statement: {}", statement);

        final var client = statement.getClient();
        log.info("Found client for statement: {}", client);

        final var appliedOffer = JsonUtil.toObject(statement.getAppliedOffer(), LoanOfferDto.class);
        log.info("Parsed applied offer: {}", appliedOffer);

        final var scoringData = getScoringDataDto(finishRegistrationRequest, client, appliedOffer);
        log.info("Constructed scoring data: {}", scoringData);

        final var creditDto = fetchFromCalculatorService(
                "/api/calculator/calc",
                scoringData,
                new ParameterizedTypeReference<SuccessDto<CreditDto>>() {
                },
                "Failed to fetch credit data from Calculator service"
        );
        log.info("Received credit data: {}", creditDto);

        final var credit = Credit.builder()
                .amount(creditDto.amount())
                .term(creditDto.term())
                .monthlyPayment(creditDto.monthlyPayment())
                .rate(creditDto.rate())
                .psk(creditDto.psk())
                .paymentSchedule(JsonUtil.toJson(creditDto.paymentSchedule()))
                .insuranceEnabled(creditDto.isInsuranceEnabled())
                .salaryClient(creditDto.isSalaryClient())
                .status(CreditStatus.CALCULATED)
                .build();
        creditRepository.save(credit);
        log.info("Saved new credit: {}", credit);

        updateClientData(client, finishRegistrationRequest);
        clientRepository.save(client);
        log.info("Updated client data: {}", client);

        statement.setCredit(credit);
        statement.setStatus(ApplicationStatus.CC_APPROVED);
        statement.getHistory().add(new StatusHistory("CC_APPROVED", Instant.now(), ChangeType.AUTOMATIC));
        statementRepository.save(statement);
        log.info("Updated statement status to CC_APPROVED: {}", statement);
    }

    private void updateClientData(Client client, FinishRegistrationRequestDto request) {
        log.info("Updating client data for client: {}", client.getId());

        client.setAccountNumber(request.accountNumber());
        client.setDependentAmount(request.dependentAmount());
        client.setGender(Gender.valueOf(request.gender()));
        log.info("Updated client basic data: accountNumber={}, dependentAmount={}, gender={}",
                request.accountNumber(), request.dependentAmount(), request.gender());

        var employment = Employment.builder()
                .id(UUID.randomUUID())
                .status(request.employment().employmentStatus())
                .employerInn(request.employment().employerINN())
                .salary(request.employment().salary())
                .position(request.employment().position())
                .workExperienceTotal(request.employment().workExperienceTotal())
                .workExperienceCurrent(request.employment().workExperienceCurrent())
                .build();
        client.setEmployment(employment);
        log.info("Updated employment details: status={}, employerInn={}, salary={}, position={}",
                request.employment().employmentStatus(), request.employment().employerINN(),
                request.employment().salary(), request.employment().position());
    }

    private ScoringDataDto getScoringDataDto(
            @NotNull final FinishRegistrationRequestDto finishRegistrationRequest,
            @NotNull final Client client,
            @NotNull final LoanOfferDto appliedOffer
    ) {
        log.info("Constructing scoring data for client: {}", client.getId());

        final var passport = client.getPassport();
        return new ScoringDataDto(
                appliedOffer.requestedAmount(),
                appliedOffer.term(),
                client.getFirstName(),
                client.getLastName(),
                client.getMiddleName(),
                finishRegistrationRequest.gender(),
                client.getBirthDate(),
                passport.getSeries(),
                passport.getNumber(),
                finishRegistrationRequest.passportIssueDate(),
                finishRegistrationRequest.passportIssueBranch(),
                finishRegistrationRequest.maritalStatus(),
                finishRegistrationRequest.dependentAmount(),
                new EmploymentDto(
                        finishRegistrationRequest.employment().employmentStatus(),
                        finishRegistrationRequest.employment().employerINN(),
                        finishRegistrationRequest.employment().salary(),
                        finishRegistrationRequest.employment().position(),
                        finishRegistrationRequest.employment().workExperienceTotal(),
                        finishRegistrationRequest.employment().workExperienceCurrent()
                ),
                finishRegistrationRequest.accountNumber(),
                appliedOffer.isInsuranceEnabled(),
                appliedOffer.isSalaryClient()
        );
    }

    private <T> T fetchFromCalculatorService(
            @NotNull final String url,
            @NotNull final Object requestBody,
            @NotNull final ParameterizedTypeReference<SuccessDto<T>> responseType,
            @NotNull final String errorMessage
    ) {
        log.info("Fetching data from Calculator service: URL={}, RequestBody={}", url, requestBody);

        final var response = calculatorRestTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                responseType
        );

        log.info("Received response from Calculator service: StatusCode={}, ResponseBody={}",
                response.getStatusCode(), response.getBody());

        return Optional.ofNullable(response.getBody())
                .filter(body -> response.getStatusCode() == HttpStatus.OK)
                .map(SuccessDto::data)
                .orElseThrow(() -> CALCULATOR_SERVICE_ERROR.get(errorMessage));
    }
}

