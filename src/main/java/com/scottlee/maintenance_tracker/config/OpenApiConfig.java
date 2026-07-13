package com.scottlee.maintenance_tracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vehicle Maintenance Tracker API")
                        .version("1.0.0")
                        .description("A RESTful API service built with Spring Boot and PostgreSQL designed to track vehicle maintenance logs and automatically calculate upcoming service alerts based on real-time mileage.")
                        .contact(new Contact()
                                .name("Scott Lee")
                                .email("lsy8418@gmail.com")
                                .url("https://github.com/scottlee-dev")));
    }
}