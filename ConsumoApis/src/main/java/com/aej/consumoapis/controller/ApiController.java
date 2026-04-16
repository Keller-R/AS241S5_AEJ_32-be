package com.aej.consumoapis.controller;

import com.aej.consumoapis.model.GooglePlacesAutocompleteResponse;
import com.aej.consumoapis.model.GooglePlacesPhotoResponse;
import com.aej.consumoapis.model.GooglePlacesResponse;
import com.aej.consumoapis.model.IpGeoLocationResponse;
import com.aej.consumoapis.service.GooglePlacesService;
import com.aej.consumoapis.service.IpGeoLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "API Consumption", description = "Endpoints para consumir APIs externas de IA")
public class ApiController {
    
    private final GooglePlacesService googlePlacesService;
    private final IpGeoLocationService ipGeoLocationService;
    
    @GetMapping("/places/search")
    @Operation(summary = "Buscar lugares", description = "Busca lugares usando Google Map Places API y almacena el resultado en la base de datos")
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
    
    @GetMapping("/geo/ip/{ipAddress}")
    @Operation(summary = "Geolocalizar IP", description = "Obtiene información geográfica de una dirección IP específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Geolocalización exitosa"),
        @ApiResponse(responseCode = "400", description = "Dirección IP inválida"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<IpGeoLocationResponse>> getLocationByIp(
            @Parameter(description = "Dirección IP a geolocalizar", example = "8.8.8.8", required = true)
            @PathVariable String ipAddress) {
        return ipGeoLocationService.getLocationByIp(ipAddress)
            .map(ResponseEntity::ok)
            .onErrorReturn(ResponseEntity.internalServerError().build());
    }
    
    @GetMapping("/places/autocomplete")
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
    
    @GetMapping("/places/{placeId}/photos/{photoReference}")
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
}
