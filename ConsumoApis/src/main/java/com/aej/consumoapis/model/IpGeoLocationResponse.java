package com.aej.consumoapis.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ip_geo_responses")
@Schema(description = "Respuesta de IP Geo Location API con información geográfica")
public class IpGeoLocationResponse {
    
    @Id
    @Schema(description = "ID único del documento en MongoDB", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Dirección IP geolocalizada", example = "8.8.8.8")
    private String ipAddress;
    
    @Schema(description = "País", example = "United States")
    private String country;
    
    @Schema(description = "Región o estado", example = "California")
    private String region;
    
    @Schema(description = "Ciudad", example = "Mountain View")
    private String city;
    
    @Schema(description = "Latitud", example = "37.4056")
    private Double latitude;
    
    @Schema(description = "Longitud", example = "-122.0775")
    private Double longitude;
    
    @Schema(description = "Zona horaria", example = "America/Los_Angeles")
    private String timezone;
    
    @Schema(description = "Proveedor de servicios de Internet", example = "Google LLC")
    private String isp;
    
    @Schema(description = "Organización", example = "Google LLC")
    private String org;
    
    @Schema(description = "Sistema autónomo", example = "AS15169 Google LLC")
    private String as;
    
    @Schema(description = "Timestamp de cuándo se realizó la consulta", example = "2024-04-06T21:35:00Z")
    private LocalDateTime timestamp;
    
    @Schema(description = "Respuesta cruda de la API en formato JSON")
    private String rawResponse;
}
