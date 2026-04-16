package com.aej.consumoapis.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI consumoApisOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Consumo APIs - AS241S5_AEJ_01-be")
                        .description("API REST reactiva para consumir servicios de Google Map Places y IP Geo Location. " +
                                "Aplicación Spring WebFlux que almacena resultados en MongoDB Atlas.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("AEJ Developer")
                                .email("developer@aej.com")
                                .url("https://github.com/aej"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de desarrollo local"),
                        new Server()
                                .url("https://api.consumo-apis.com")
                                .description("Servidor de producción")
                ));
    }
}
