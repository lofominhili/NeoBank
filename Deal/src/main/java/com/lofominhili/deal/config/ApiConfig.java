package com.lofominhili.deal.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Deal Api",
                description = "Api for the Deal", version = "1.0.0",
                contact = @Contact(
                        name = "lofominhili",
                        url = "https://github.com/lofominhili/NeoBank/Deal"
                )
        )
)
public class ApiConfig {
}
