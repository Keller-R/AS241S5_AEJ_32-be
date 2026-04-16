package com.aej.consumoapis.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "google_places_autocomplete")
@Schema(description = "Respuesta de Google Places Autocomplete API con sugerencias de lugares")
public class GooglePlacesAutocompleteResponse {
    
    @Id
    @Schema(description = "ID único del documento en MongoDB", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Texto de búsqueda ingresado", example = "San vicente de cañete")
    private String input;
    
    @Schema(description = "Lista de sugerencias encontradas")
    private List<Suggestion> suggestions;
    
    @Schema(description = "Timestamp de cuándo se realizó la consulta", example = "2024-04-06T21:35:00Z")
    private LocalDateTime timestamp;
    
    @Schema(description = "Respuesta cruda de la API en formato JSON")
    private String rawResponse;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Sugerencia de lugar individual")
    public static class Suggestion {
        @Schema(description = "ID único del lugar", example = "ChIJPwW-XUn5D5ERRHI_dTXC9BA")
        private String placeId;
        
        @Schema(description = "Texto completo de la sugerencia", example = "San Vicente de Cañete, Perú")
        private String text;
        
        @Schema(description = "Texto principal", example = "San Vicente de Cañete")
        private String mainText;
        
        @Schema(description = "Texto secundario", example = "Perú")
        private String secondaryText;
        
        @Schema(description = "Tipos de lugar", example = "[\"locality\", \"geocode\", \"political\"]")
        private List<String> types;
        
        @Schema(description = "Distancia en metros", example = "8533609")
        private Integer distanceMeters;
    }
}
