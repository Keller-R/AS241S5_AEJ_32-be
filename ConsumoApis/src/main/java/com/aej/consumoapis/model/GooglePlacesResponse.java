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
@Document(collection = "google_places_responses")
@Schema(description = "Respuesta de Google Places API con información de lugares encontrados")
public class GooglePlacesResponse {
    
    @Id
    @Schema(description = "ID único del documento en MongoDB", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "Consulta de búsqueda realizada", example = "restaurantes en madrid")
    private String query;
    
    @Schema(description = "Estado de la respuesta de la API", example = "OK")
    private String status;
    
    @Schema(description = "Lista de lugares encontrados")
    private List<Place> places;
    
    @Schema(description = "Timestamp de cuándo se realizó la consulta", example = "2024-04-06T21:35:00Z")
    private LocalDateTime timestamp;
    
    @Schema(description = "Respuesta cruda de la API en formato JSON")
    private String rawResponse;
    
    @Schema(description = "Indica si el registro ha sido eliminado lógicamente", example = "false")
    private Boolean deleted = false;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Información de un lugar específico")
    public static class Place {
        @Schema(description = "ID único del lugar", example = "ChIJrTLr-GyuEmsRBfyf1GD8ETkU")
        private String placeId;
        
        @Schema(description = "Nombre del lugar", example = "Restaurante Botín")
        private String name;
        
        @Schema(description = "Dirección completa formateada", example = "C. de Cuchilleros, 17, 28005 Madrid, España")
        private String formattedAddress;
        
        @Schema(description = "Latitud", example = "40.4138")
        private Double latitude;
        
        @Schema(description = "Longitud", example = "-3.7133")
        private Double longitude;
        
        @Schema(description = "Calificación del lugar", example = "4.5")
        private String rating;
        
        @Schema(description = "Tipos de lugar", example = "[\"restaurant\", \"food\", \"point_of_interest\"]")
        private List<String> types;
        
        @Schema(description = "Vecindario o área", example = "Centro")
        private String vicinity;
        
        @Schema(description = "Lista de fotos del lugar")
        private List<Photo> photos;
        
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "Información de una foto del lugar")
        public static class Photo {
            @Schema(description = "Nombre del autor de la foto", example = "Juan Pérez")
            private String authorAttributions;
            
            @Schema(description = "Referencia de la foto para obtenerla", example = "AUacShh3_Dd8yvV2JZMtNjjbbSbFhSv-0VmUN-uasQ2Oj00XB63irPTks0-A_1rMNfdTunoOVZfVOExRRBNrupUf8TY4Kw5iQNQgf2rwcaM8hXNQg7KDyvMR5B-HzoCE1mwy2ba9yxvmtiJrdV-xBgO8c5iJL65BCd0slyI1")
            private String photoReference;
            
            @Schema(description = "Altura de la foto", example = "3024")
            private Integer heightPx;
            
            @Schema(description = "Ancho de la foto", example = "4032")
            private Integer widthPx;
        }
    }
}
