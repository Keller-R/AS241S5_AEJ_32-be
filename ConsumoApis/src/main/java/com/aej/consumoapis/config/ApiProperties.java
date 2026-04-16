package com.aej.consumoapis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "apis")
public class ApiProperties {
    
    private GoogleMaps googleMaps;
    private IpGeo ipGeo;
    
    @Data
    public static class GoogleMaps {
        private String rapidapiKey;
        private String baseUrl;
    }
    
    @Data
    public static class IpGeo {
        private String rapidapiKey;
        private String baseUrl;
    }
}
