package com.rentaloc.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

@Configuration
//@SecurityScheme(
//        name = "Bearer Authentication",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
//@OpenAPIDefinition(
//        info =@Info(
//                title = "Rental API",
//                version = "${api.version}",
//                contact = @Contact(
//                        name = "Baeldung", email = "user-apis@baeldung.com", url = "https://www.baeldung.com"
//                ),
//                license = @License(
//                        name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
//                ),
//                termsOfService = "${tos.uri}",
//                description = "${api.description}"
//        ),
//        servers = @Server(
//                url = "${api.server.url}",
//                description = "Production"
//        )
//)
public class SwaggerConfig {

    @Bean
    public OpenAPI myAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .info(getApiInfo())
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList("bearer-jwt", Arrays.asList("read", "write"))
                                .addList("bearer-key", Collections.emptyList())
                );
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
