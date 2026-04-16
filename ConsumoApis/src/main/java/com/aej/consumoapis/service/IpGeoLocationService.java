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
}
