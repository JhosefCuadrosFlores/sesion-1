package com.pagatu.base.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pagatuOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PagaTú - Servicio Base API")
                        .description("Sesión 1: Construcción del Servicio Base. Entidad principal Usuario/Cuenta.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cuadros Flores Jhosef Giampiere")
                                .email("jhosef.cuadros@pagatu.edu.pe")));
    }
}
