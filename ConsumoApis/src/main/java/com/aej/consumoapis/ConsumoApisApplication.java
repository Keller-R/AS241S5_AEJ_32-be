package com.aej.consumoapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConsumoApisApplication {

    public static void main(String[] args) {
        // Configurar propiedades SSL ANTES de iniciar Spring Boot
        // Esto soluciona el problema de "internal_error" con MongoDB Atlas en Java 17/21
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
        System.setProperty("https.protocols", "TLSv1.2");
        
        SpringApplication.run(ConsumoApisApplication.class, args);
    }

}
