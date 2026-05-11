package com.aej.consumoapis.controller;

import com.aej.consumoapis.model.GooglePlacesAutocompleteResponse;
import com.aej.consumoapis.model.GooglePlacesPhotoResponse;
import com.aej.consumoapis.model.GooglePlacesResponse;
import com.aej.consumoapis.service.GooglePlacesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Google Places API", description = "Endpoints para consumir Google Places API - Búsqueda de lugares, autocompletado y fotos")
public class GooglePlacesController {
    
    private final GooglePlacesService googlePlacesService;
    
    // ==================== OPERACIONES DE BÚSQUEDA ====================
    
    @GetMapping("/search")
    @Operation(summary = "Buscar lugares", description = "Busca lugares usando Google Places API y almacena el resultado en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda exitosa"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<GooglePlacesResponse>> searchPlaces(
            @Parameter(description = "Texto de búsqueda para lugares", example = "restaurantes en madrid", required = true)
            @RequestParam String query) {
        return googlePlacesService.searchPlaces(query)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.internalServerError().build());
    }
    
    @GetMapping("/autocomplete")
    @Operation(summary = "Autocompletar lugares", description = "Obtiene sugerencias de autocompletado de lugares basadas en el texto ingresado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sugerencias obtenidas exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<GooglePlacesAutocompleteResponse>> autocompletePlaces(
            @Parameter(description = "Texto de entrada para autocompletar", example = "San vicente de cañete", required = true)
            @RequestParam String input) {
        return googlePlacesService.autocompletePlaces(input)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.internalServerError().build());
    }
    
    @GetMapping("/{placeId}/photos/{photoReference}")
    @Operation(summary = "Obtener foto de lugar", description = "Obtiene la foto de un lugar específico usando su ID y referencia de foto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Foto obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<GooglePlacesPhotoResponse>> getPlacePhoto(
            @Parameter(description = "ID del lugar", example = "ChIJ2fzCmcW7j4AR2JzfXBBoh6E", required = true)
            @PathVariable String placeId,
            @Parameter(description = "Referencia de la foto", example = "AUacShh3_Dd8yvV2JZMtNjjbbSbFhSv-0VmUN-uasQ2Oj00XB63irPTks0-A_1rMNfdTunoOVZfVOExRRBNrupUf8TY4Kw5iQNQgf2rwcaM8hXNQg7KDyvMR5B-HzoCE1mwy2ba9yxvmtiJrdV-xBgO8c5iJL65BCd0slyI1", required = true)
            @PathVariable String photoReference,
            @Parameter(description = "Ancho máximo de la foto", example = "400")
            @RequestParam(required = false, defaultValue = "400") Integer maxWidthPx,
            @Parameter(description = "Alto máximo de la foto", example = "400")
            @RequestParam(required = false, defaultValue = "400") Integer maxHeightPx) {
        return googlePlacesService.getPlacePhoto(placeId, photoReference, maxWidthPx, maxHeightPx)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.internalServerError().build());
    }
    
    // ==================== CRUD - GESTIÓN DE CONSULTAS ====================
    
    @GetMapping("/queries")
    @Operation(summary = "Listar todas las búsquedas de lugares", description = "Obtiene todas las búsquedas de lugares almacenadas que no han sido eliminadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsquedas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Flux<GooglePlacesResponse> getAllPlacesQueries() {
        return googlePlacesService.getAllPlacesQueries();
    }
    
    @GetMapping("/queries/{id}")
    @Operation(summary = "Obtener búsqueda por ID", description = "Obtiene una búsqueda de lugares específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Búsqueda no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<GooglePlacesResponse>> getPlacesQueryById(
            @Parameter(description = "ID de la búsqueda", example = "507f1f77bcf86cd799439011", required = true)
            @PathVariable String id) {
        return googlePlacesService.getPlacesQueryById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/queries/{id}")
    @Operation(summary = "Actualizar búsqueda de lugares", description = "Actualiza una búsqueda existente con una nueva consulta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Búsqueda no encontrada"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<GooglePlacesResponse>> updatePlacesQuery(
            @Parameter(description = "ID de la búsqueda a actualizar", example = "507f1f77bcf86cd799439011", required = true)
            @PathVariable String id,
            @Parameter(description = "Nueva consulta de búsqueda", example = "hoteles en lima", required = true)
            @RequestParam String newQuery) {
        return googlePlacesService.updatePlacesQuery(id, newQuery)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/queries/{id}")
    @Operation(summary = "Eliminar búsqueda de lugares", description = "Realiza un borrado lógico de una búsqueda de lugares")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Búsqueda eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Búsqueda no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<Void>> deletePlacesQuery(
            @Parameter(description = "ID de la búsqueda a eliminar", example = "507f1f77bcf86cd799439011", required = true)
            @PathVariable String id) {
        return googlePlacesService.deletePlacesQuery(id)
            .then(Mono.just(ResponseEntity.noContent().<Void>build()))
            .onErrorReturn(ResponseEntity.notFound().build());
    }
}
