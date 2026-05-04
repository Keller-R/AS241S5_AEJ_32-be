package com.aej.consumoapis.config;

import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

/**
 * Configuración de MongoDB para solucionar problemas de SSL
 * con Java 17/21 y MongoDB Atlas
 */
@Configuration
public class MongoConfig {

    @PostConstruct
    public void init() {
        // Configurar propiedades del sistema para SSL
        // Esto soluciona el problema de "internal_error" con MongoDB Atlas
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
        System.setProperty("https.protocols", "TLSv1.2");
    }
}
