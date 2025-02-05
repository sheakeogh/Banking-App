package com.bank.backend.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI registrationOpenAPI() {
        return new OpenAPI().info(new Info().title("Bank of Shea API")
                .description("Bank of Shea API Reference for Developers")
                .version("1.0"));
    }
}