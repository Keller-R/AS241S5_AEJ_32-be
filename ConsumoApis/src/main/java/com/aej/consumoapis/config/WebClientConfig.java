package com.aej.consumoapis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder(ObjectMapper objectMapper) {
        return WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024));
    }
}
