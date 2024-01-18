package com.rentaloc.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI myAPI() {
        return new OpenAPI()
                .info(getApiInfo());
    }


    private Info getApiInfo() {
        return new Info()
                .title("Application backend Rental API")
                .description("localhost:3001/")
                .version("1.0")
                .contact(getContacts())
                .license(new License().name("Apache 2.0").url("http://springdoc.org"));
    }

    private Contact getContacts() {
        return new Contact()
                .name("Rental");
    }
}
