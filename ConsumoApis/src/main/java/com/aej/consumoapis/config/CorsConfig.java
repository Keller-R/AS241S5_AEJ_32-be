package com.aej.consumoapis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de CORS para permitir peticiones desde Swagger UI
 * y otros orígenes (especialmente útil en Codespaces)
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Permitir todos los orígenes (para desarrollo)
        // En producción, especifica los dominios permitidos
        corsConfig.setAllowedOriginPatterns(Arrays.asList("*"));
        
        // Permitir todos los métodos HTTP
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Permitir todos los headers
        corsConfig.setAllowedHeaders(Arrays.asList("*"));
        
        // Permitir credenciales
        corsConfig.setAllowCredentials(true);
        
        // Tiempo de cache para preflight requests
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
