package com.aej.consumoapis.controller;

import com.aej.consumoapis.model.IpGeoLocationResponse;
import com.aej.consumoapis.service.IpGeoLocationService;
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
@RequestMapping("/api/v1/geo")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "IP Geo Location API", description = "Endpoints para consumir IP Geo Location API - Geolocalización de direcciones IP")
public class IpGeoLocationController {
    
    private final IpGeoLocationService ipGeoLocationService;
    
    // ==================== OPERACIONES DE GEOLOCALIZACIÓN ====================
    
    @GetMapping("/ip/{ipAddress}")
    @Operation(summary = "Geolocalizar IP", description = "Obtiene información geográfica de una dirección IP específica y la almacena en la base de datos")
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
    
    // ==================== CRUD - GESTIÓN DE CONSULTAS ====================
    
    @GetMapping("/queries")
    @Operation(summary = "Listar todas las consultas de geolocalización", description = "Obtiene todas las consultas de geolocalización almacenadas que no han sido eliminadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consultas obtenidas exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Flux<IpGeoLocationResponse> getAllGeoQueries() {
        return ipGeoLocationService.getAllGeoQueries();
    }
    
    @GetMapping("/queries/{id}")
    @Operation(summary = "Obtener consulta por ID", description = "Obtiene una consulta de geolocalización específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<IpGeoLocationResponse>> getGeoQueryById(
            @Parameter(description = "ID de la consulta", example = "507f1f77bcf86cd799439011", required = true)
            @PathVariable String id) {
        return ipGeoLocationService.getGeoQueryById(id)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/queries/{id}")
    @Operation(summary = "Actualizar consulta de geolocalización", description = "Actualiza una consulta existente con una nueva dirección IP")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
        @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<IpGeoLocationResponse>> updateGeoQuery(
            @Parameter(description = "ID de la consulta a actualizar", example = "507f1f77bcf86cd799439011", required = true)
            @PathVariable String id,
            @Parameter(description = "Nueva dirección IP", example = "1.1.1.1", required = true)
            @RequestParam String newIpAddress) {
        return ipGeoLocationService.updateGeoQuery(id, newIpAddress)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/queries/{id}")
    @Operation(summary = "Eliminar consulta de geolocalización", description = "Realiza un borrado lógico de una consulta de geolocalización")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Consulta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public Mono<ResponseEntity<Void>> deleteGeoQuery(
            @Parameter(description = "ID de la consulta a eliminar", example = "507f1f77bcf86cd799439011", required = true)
            @PathVariable String id) {
        return ipGeoLocationService.deleteGeoQuery(id)
            .then(Mono.just(ResponseEntity.noContent().<Void>build()))
            .onErrorReturn(ResponseEntity.notFound().build());
    }
}
