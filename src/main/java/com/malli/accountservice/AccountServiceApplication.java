package com.malli.accountservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Account Service", description = "This is a Spring Boot Account Microservice"))
/*@OpenAPIDefinition(info = @Info(title = "Account Service", description = "This is a Spring Boot Account Microservice"),
        security = @SecurityRequirement(name = "AUTHORIZATION"))
@SecurityScheme(name = "AUTHORIZATION", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)*/
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
