package com.lofominhili.calculator.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TestProperties.class)
public class TestConfiguration {

}
