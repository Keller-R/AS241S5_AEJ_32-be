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
@Document(collection = "google_places_photos")
@Schema(description = "Respuesta de Google Places Photos API con información de fotos de lugares")
public class GooglePlacesPhotoResponse {
    
    @Id
    @Schema(description = "ID único del documento en MongoDB", example = "507f1f77bcf86cd799439011")
    private String id;
    
    @Schema(description = "ID del lugar", example = "ChIJ2fzCmcW7j4AR2JzfXBBoh6E")
    private String placeId;
    
    @Schema(description = "Referencia de la foto", example = "AUacShh3_Dd8yvV2JZMtNjjbbSbFhSv-0VmUN-uasQ2Oj00XB63irPTks0-A_1rMNfdTunoOVZfVOExRRBNrupUf8TY4Kw5iQNQgf2rwcaM8hXNQg7KDyvMR5B-HzoCE1mwy2ba9yxvmtiJrdV-xBgO8c5iJL65BCd0slyI1")
    private String photoReference;
    
    @Schema(description = "URL de la foto", example = "https://lh3.googleusercontent.com/...")
    private String photoUrl;
    
    @Schema(description = "Ancho máximo solicitado", example = "400")
    private Integer maxWidthPx;
    
    @Schema(description = "Alto máximo solicitado", example = "400")
    private Integer maxHeightPx;
    
    @Schema(description = "Tipo de contenido MIME", example = "image/jpeg")
    private String contentType;
    
    @Schema(description = "Timestamp de cuándo se realizó la consulta", example = "2024-04-06T21:35:00Z")
    private LocalDateTime timestamp;
    
    @Schema(description = "Respuesta cruda de la API")
    private String rawResponse;
}
