package com.lofominhili.deal.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lofominhili.deal.dto.basic.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper;

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().isError();
    }

    @Override
    public void handleError(@NonNull ClientHttpResponse httpResponse) throws IOException {
        var statusCode = httpResponse.getStatusCode();
        if (statusCode.is5xxServerError() || statusCode.equals(HttpStatus.NOT_FOUND)) {
            log.error("Error status code {}", statusCode);
            return;
        }

        var response = parseResponse(httpResponse);
        //TODO добавить обработку возможных ошибок
        log.error("Error in response {}", response);
    }

    public ErrorMessage parseResponse(ClientHttpResponse httpResponse) throws IOException {
        return objectMapper.readValue(new String(httpResponse.getBody().readAllBytes()), ErrorMessage.class);
    }
}
