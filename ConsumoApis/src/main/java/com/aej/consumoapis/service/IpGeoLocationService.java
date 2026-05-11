package com.aej.consumoapis.service;

import com.aej.consumoapis.config.ApiProperties;
import com.aej.consumoapis.model.IpGeoLocationResponse;
import com.aej.consumoapis.repository.IpGeoLocationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class IpGeoLocationService {
    
    private final ApiProperties apiProperties;
    private final IpGeoLocationRepository repository;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    
    public Mono<IpGeoLocationResponse> getLocationByIp(String ipAddress) {
        log.info("Getting location for IP: {}", ipAddress);
        
        WebClient webClient = webClientBuilder
            .defaultHeader("X-RapidAPI-Key", apiProperties.getIpGeo().getRapidapiKey())
            .defaultHeader("X-RapidAPI-Host", "ip-geo-location10.p.rapidapi.com")
            .build();
        
        return webClient.get()
            .uri(apiProperties.getIpGeo().getBaseUrl() + "/ip?ip=" + ipAddress)
            .retrieve()
            .bodyToMono(String.class)
            .map(this::parseResponse)
            .map(response -> {
                response.setIpAddress(ipAddress);
                response.setTimestamp(LocalDateTime.now());
                response.setDeleted(false);
                return response;
            })
            .flatMap(repository::save)
            .doOnSuccess(response -> log.info("Successfully saved IP geo location response for IP: {}", ipAddress))
            .doOnError(error -> log.error("Error getting location for IP: {}", ipAddress, error));
    }
    
    public Mono<IpGeoLocationResponse> getCurrentIpLocation() {
        log.info("Getting location for current IP");
        
        WebClient webClient = webClientBuilder
            .defaultHeader("X-RapidAPI-Key", apiProperties.getIpGeo().getRapidapiKey())
            .defaultHeader("X-RapidAPI-Host", "ip-geo-location10.p.rapidapi.com")
            .build();
        
        return webClient.get()
            .uri(apiProperties.getIpGeo().getBaseUrl() + "/ip")
            .retrieve()
            .bodyToMono(String.class)
            .map(this::parseResponse)
            .map(response -> {
                response.setIpAddress("current");
                response.setTimestamp(LocalDateTime.now());
                return response;
            })
            .flatMap(repository::save)
            .doOnSuccess(response -> log.info("Successfully saved current IP geo location response"))
            .doOnError(error -> log.error("Error getting current IP location", error));
    }
    
    private IpGeoLocationResponse parseResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            IpGeoLocationResponse response = new IpGeoLocationResponse();
            
            // RapidAPI returns data in "result" object
            JsonNode result = root.has("result") ? root.get("result") : root;
            
            if (result.has("ip")) {
                response.setIpAddress(result.get("ip").asText());
            }
            if (result.has("country")) {
                response.setCountry(result.get("country").asText());
            }
            if (result.has("region")) {
                response.setRegion(result.get("region").asText());
            }
            if (result.has("city")) {
                response.setCity(result.get("city").asText());
            }
            if (result.has("latitude")) {
                response.setLatitude(result.get("latitude").asDouble());
            }
            if (result.has("longitude")) {
                response.setLongitude(result.get("longitude").asDouble());
            }
            if (result.has("time_zone")) {
                response.setTimezone(result.get("time_zone").asText());
            }
            if (result.has("country_code")) {
                response.setCountry(result.get("country").asText() + " (" + result.get("country_code").asText() + ")");
            }
            
            response.setRawResponse(jsonResponse);
            
            return response;
        } catch (Exception e) {
            log.error("Error parsing IP Geo Location response", e);
            IpGeoLocationResponse errorResponse = new IpGeoLocationResponse();
            errorResponse.setCountry("PARSE_ERROR");
            errorResponse.setRawResponse(jsonResponse);
            return errorResponse;
        }
    }
    
    /**
     * Obtiene todas las consultas de geolocalización que no han sido eliminadas
     */
    public Flux<IpGeoLocationResponse> getAllGeoQueries() {
        log.info("Getting all geo queries");
        return repository.findByDeletedFalseOrDeletedIsNull()
            .doOnComplete(() -> log.info("Successfully retrieved all geo queries"))
            .doOnError(error -> log.error("Error getting all geo queries", error));
    }
    
    /**
     * Obtiene una consulta de geolocalización por ID
     */
    public Mono<IpGeoLocationResponse> getGeoQueryById(String id) {
        log.info("Getting geo query by ID: {}", id);
        return repository.findById(id)
            .filter(response -> response.getDeleted() == null || !response.getDeleted())
            .doOnSuccess(response -> {
                if (response != null) {
                    log.info("Successfully retrieved geo query with ID: {}", id);
                } else {
                    log.warn("Geo query with ID {} not found or is deleted", id);
                }
            })
            .doOnError(error -> log.error("Error getting geo query by ID: {}", id, error));
    }
    
    /**
     * Actualiza una consulta de geolocalización con una nueva IP
     */
    public Mono<IpGeoLocationResponse> updateGeoQuery(String id, String newIpAddress) {
        log.info("Updating geo query with ID: {} with new IP: {}", id, newIpAddress);
        
        return repository.findById(id)
            .filter(response -> response.getDeleted() == null || !response.getDeleted())
            .flatMap(existingResponse -> {
                // Realizar nueva consulta a la API con la nueva IP
                return getLocationByIp(newIpAddress)
                    .map(newResponse -> {
                        // Mantener el ID original
                        newResponse.setId(id);
                        return newResponse;
                    })
                    .flatMap(repository::save);
            })
            .doOnSuccess(response -> log.info("Successfully updated geo query with ID: {}", id))
            .doOnError(error -> log.error("Error updating geo query with ID: {}", id, error));
    }
    
    /**
     * Realiza un borrado lógico de una consulta de geolocalización
     */
    public Mono<Void> deleteGeoQuery(String id) {
        log.info("Deleting geo query with ID: {}", id);
        
        return repository.findById(id)
            .flatMap(response -> {
                response.setDeleted(true);
                return repository.save(response);
            })
            .then()
            .doOnSuccess(v -> log.info("Successfully deleted geo query with ID: {}", id))
            .doOnError(error -> log.error("Error deleting geo query with ID: {}", id, error));
    }
}
