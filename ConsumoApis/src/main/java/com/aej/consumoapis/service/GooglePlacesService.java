package com.aej.consumoapis.service;

import com.aej.consumoapis.config.ApiProperties;
import com.aej.consumoapis.model.GooglePlacesAutocompleteResponse;
import com.aej.consumoapis.model.GooglePlacesPhotoResponse;
import com.aej.consumoapis.model.GooglePlacesResponse;
import com.aej.consumoapis.repository.GooglePlacesAutocompleteRepository;
import com.aej.consumoapis.repository.GooglePlacesPhotoRepository;
import com.aej.consumoapis.repository.GooglePlacesRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GooglePlacesService {
    
    private final ApiProperties apiProperties;
    private final GooglePlacesRepository repository;
    private final GooglePlacesAutocompleteRepository autocompleteRepository;
    private final GooglePlacesPhotoRepository photoRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    
    public Mono<GooglePlacesResponse> searchPlaces(String query) {
        log.info("Searching places for query: {}", query);
        
        WebClient webClient = webClientBuilder
            .defaultHeader("X-RapidAPI-Key", apiProperties.getGoogleMaps().getRapidapiKey())
            .defaultHeader("X-RapidAPI-Host", "google-map-places-new-v2.p.rapidapi.com")
            .build();
        
        // Construir el JSON body para Text Search
        String jsonBody = String.format(
            "{\"textQuery\":\"%s\",\"languageCode\":\"es\",\"regionCode\":\"PE\",\"maxResultCount\":10}",
            query.replace("\"", "\\\"")
        );
        
        return webClient.post()
            .uri(apiProperties.getGoogleMaps().getBaseUrl() + "/v1/places:searchText")
            .header("Content-Type", "application/json")
            .header("X-Goog-FieldMask", "places.id,places.displayName,places.formattedAddress,places.location,places.rating,places.types,places.photos")
            .bodyValue(jsonBody)
            .retrieve()
            .bodyToMono(String.class)
            .map(this::parseResponse)
            .map(response -> {
                response.setQuery(query);
                response.setTimestamp(LocalDateTime.now());
                response.setDeleted(false);
                return response;
            })
            .flatMap(repository::save)
            .doOnSuccess(response -> log.info("Successfully saved places response for query: {}", query))
            .doOnError(error -> log.error("Error searching places for query: {}", query, error));
    }
    
    private GooglePlacesResponse parseResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            GooglePlacesResponse response = new GooglePlacesResponse();
            
            // Verificar si hay error o respuesta exitosa
            if (root.has("error")) {
                response.setStatus("ERROR: " + root.get("error").get("message").asText());
            } else {
                response.setStatus("OK");
            }
            
            List<GooglePlacesResponse.Place> places = new ArrayList<>();
            if (root.has("places")) {
                JsonNode placesNode = root.get("places");
                for (JsonNode placeNode : placesNode) {
                    GooglePlacesResponse.Place place = new GooglePlacesResponse.Place();
                    
                    if (placeNode.has("id")) {
                        place.setPlaceId(placeNode.get("id").asText());
                    }
                    if (placeNode.has("displayName")) {
                        JsonNode displayName = placeNode.get("displayName");
                        if (displayName.has("text")) {
                            place.setName(displayName.get("text").asText());
                        }
                    }
                    if (placeNode.has("formattedAddress")) {
                        place.setFormattedAddress(placeNode.get("formattedAddress").asText());
                    }
                    if (placeNode.has("location")) {
                        JsonNode location = placeNode.get("location");
                        if (location.has("latitude")) {
                            place.setLatitude(location.get("latitude").asDouble());
                        }
                        if (location.has("longitude")) {
                            place.setLongitude(location.get("longitude").asDouble());
                        }
                    }
                    if (placeNode.has("rating")) {
                        place.setRating(String.valueOf(placeNode.get("rating").asDouble()));
                    }
                    if (placeNode.has("types")) {
                        List<String> types = new ArrayList<>();
                        for (JsonNode typeNode : placeNode.get("types")) {
                            types.add(typeNode.asText());
                        }
                        place.setTypes(types);
                    }
                    
                    // Parse photos
                    if (placeNode.has("photos")) {
                        List<GooglePlacesResponse.Place.Photo> photos = new ArrayList<>();
                        for (JsonNode photoNode : placeNode.get("photos")) {
                            GooglePlacesResponse.Place.Photo photo = new GooglePlacesResponse.Place.Photo();
                            
                            if (photoNode.has("name")) {
                                // El name contiene la referencia completa: places/PLACE_ID/photos/PHOTO_REFERENCE
                                String fullPhotoName = photoNode.get("name").asText();
                                // Extraer solo la referencia de la foto
                                photo.setPhotoReference(fullPhotoName);
                            }
                            if (photoNode.has("heightPx")) {
                                photo.setHeightPx(photoNode.get("heightPx").asInt());
                            }
                            if (photoNode.has("widthPx")) {
                                photo.setWidthPx(photoNode.get("widthPx").asInt());
                            }
                            if (photoNode.has("authorAttributions")) {
                                JsonNode authorNode = photoNode.get("authorAttributions");
                                if (authorNode.isArray() && authorNode.size() > 0) {
                                    JsonNode firstAuthor = authorNode.get(0);
                                    if (firstAuthor.has("displayName")) {
                                        photo.setAuthorAttributions(firstAuthor.get("displayName").asText());
                                    }
                                }
                            }
                            
                            photos.add(photo);
                        }
                        place.setPhotos(photos);
                    }
                    
                    places.add(place);
                }
            }
            
            response.setPlaces(places);
            response.setRawResponse(jsonResponse);
            
            return response;
        } catch (Exception e) {
            log.error("Error parsing Google Places response", e);
            GooglePlacesResponse errorResponse = new GooglePlacesResponse();
            errorResponse.setStatus("PARSE_ERROR");
            errorResponse.setRawResponse(jsonResponse);
            return errorResponse;
        }
    }
    
    public Mono<GooglePlacesAutocompleteResponse> autocompletePlaces(String input) {
        log.info("Getting autocomplete suggestions for input: {}", input);
        
        WebClient webClient = webClientBuilder
            .defaultHeader("X-RapidAPI-Key", apiProperties.getGoogleMaps().getRapidapiKey())
            .defaultHeader("X-RapidAPI-Host", "google-map-places-new-v2.p.rapidapi.com")
            .build();
        
        // Construir el JSON body para Autocomplete
        String jsonBody = String.format(
            "{\"input\":\"%s\",\"languageCode\":\"es\",\"regionCode\":\"PE\"}",
            input.replace("\"", "\\\"")
        );
        
        return webClient.post()
            .uri(apiProperties.getGoogleMaps().getBaseUrl() + "/v1/places:autocomplete")
            .header("Content-Type", "application/json")
            .header("X-Goog-FieldMask", "suggestions.placePrediction.place,suggestions.placePrediction.placeId,suggestions.placePrediction.text,suggestions.placePrediction.structuredFormat,suggestions.placePrediction.types")
            .bodyValue(jsonBody)
            .retrieve()
            .bodyToMono(String.class)
            .map(this::parseAutocompleteResponse)
            .map(response -> {
                response.setInput(input);
                response.setTimestamp(LocalDateTime.now());
                return response;
            })
            .flatMap(autocompleteRepository::save)
            .doOnSuccess(response -> log.info("Successfully saved autocomplete response for input: {}", input))
            .doOnError(error -> log.error("Error getting autocomplete for input: {}", input, error));
    }
    
    private GooglePlacesAutocompleteResponse parseAutocompleteResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            GooglePlacesAutocompleteResponse response = new GooglePlacesAutocompleteResponse();
            
            List<GooglePlacesAutocompleteResponse.Suggestion> suggestions = new ArrayList<>();
            
            if (root.has("suggestions")) {
                JsonNode suggestionsNode = root.get("suggestions");
                for (JsonNode suggestionNode : suggestionsNode) {
                    if (suggestionNode.has("placePrediction")) {
                        JsonNode placePrediction = suggestionNode.get("placePrediction");
                        GooglePlacesAutocompleteResponse.Suggestion suggestion = new GooglePlacesAutocompleteResponse.Suggestion();
                        
                        if (placePrediction.has("placeId")) {
                            suggestion.setPlaceId(placePrediction.get("placeId").asText());
                        }
                        if (placePrediction.has("text")) {
                            JsonNode textNode = placePrediction.get("text");
                            if (textNode.has("text")) {
                                suggestion.setText(textNode.get("text").asText());
                            }
                        }
                        if (placePrediction.has("structuredFormat")) {
                            JsonNode structuredFormat = placePrediction.get("structuredFormat");
                            if (structuredFormat.has("mainText")) {
                                JsonNode mainText = structuredFormat.get("mainText");
                                if (mainText.has("text")) {
                                    suggestion.setMainText(mainText.get("text").asText());
                                }
                            }
                            if (structuredFormat.has("secondaryText")) {
                                JsonNode secondaryText = structuredFormat.get("secondaryText");
                                if (secondaryText.has("text")) {
                                    suggestion.setSecondaryText(secondaryText.get("text").asText());
                                }
                            }
                        }
                        if (placePrediction.has("types")) {
                            List<String> types = new ArrayList<>();
                            for (JsonNode typeNode : placePrediction.get("types")) {
                                types.add(typeNode.asText());
                            }
                            suggestion.setTypes(types);
                        }
                        if (placePrediction.has("distanceMeters")) {
                            suggestion.setDistanceMeters(placePrediction.get("distanceMeters").asInt());
                        }
                        
                        suggestions.add(suggestion);
                    }
                }
            }
            
            response.setSuggestions(suggestions);
            response.setRawResponse(jsonResponse);
            
            return response;
        } catch (Exception e) {
            log.error("Error parsing Google Places Autocomplete response", e);
            GooglePlacesAutocompleteResponse errorResponse = new GooglePlacesAutocompleteResponse();
            errorResponse.setRawResponse(jsonResponse);
            return errorResponse;
        }
    }
    
    public Mono<GooglePlacesPhotoResponse> getPlacePhoto(String placeId, String photoReference, Integer maxWidthPx, Integer maxHeightPx) {
        log.info("Getting photo for placeId: {}, photoReference: {}", placeId, photoReference);
        
        WebClient webClient = webClientBuilder
            .defaultHeader("X-RapidAPI-Key", apiProperties.getGoogleMaps().getRapidapiKey())
            .defaultHeader("X-RapidAPI-Host", "google-map-places-new-v2.p.rapidapi.com")
            .build();
        
        // Extraer solo el ID de la foto del photoReference completo
        // El photoReference viene como: places/PLACE_ID/photos/PHOTO_ID
        // Necesitamos solo: PHOTO_ID
        String photoId = photoReference;
        if (photoReference.contains("/photos/")) {
            photoId = photoReference.substring(photoReference.lastIndexOf("/photos/") + 8);
        }
        
        log.info("Extracted photoId: {}", photoId);
        
        // Construir la URL con parámetros
        String uri = String.format("%s/v1/places/%s/photos/%s/media?maxWidthPx=%d&maxHeightPx=%d&skipHttpRedirect=true",
            apiProperties.getGoogleMaps().getBaseUrl(),
            placeId,
            photoId,
            maxWidthPx != null ? maxWidthPx : 400,
            maxHeightPx != null ? maxHeightPx : 400
        );
        
        return webClient.get()
            .uri(uri)
            .header("Content-Type", "application/json")
            .retrieve()
            .bodyToMono(String.class)
            .map(responseBody -> {
                GooglePlacesPhotoResponse response = new GooglePlacesPhotoResponse();
                response.setPlaceId(placeId);
                response.setPhotoReference(photoReference);
                response.setMaxWidthPx(maxWidthPx != null ? maxWidthPx : 400);
                response.setMaxHeightPx(maxHeightPx != null ? maxHeightPx : 400);
                response.setTimestamp(LocalDateTime.now());
                response.setRawResponse(responseBody);
                
                // Intentar extraer la URL de la foto del JSON de respuesta
                try {
                    JsonNode root = objectMapper.readTree(responseBody);
                    if (root.has("photoUri")) {
                        response.setPhotoUrl(root.get("photoUri").asText());
                    }
                    if (root.has("contentType")) {
                        response.setContentType(root.get("contentType").asText());
                    }
                } catch (Exception e) {
                    log.warn("Could not parse photo response as JSON, might be binary data");
                }
                
                return response;
            })
            .flatMap(photoRepository::save)
            .doOnSuccess(response -> log.info("Successfully saved photo response for placeId: {}", placeId))
            .doOnError(error -> log.error("Error getting photo for placeId: {}", placeId, error));
    }
    
    /**
     * Obtiene todas las búsquedas de lugares que no han sido eliminadas
     */
    public Flux<GooglePlacesResponse> getAllPlacesQueries() {
        log.info("Getting all places queries");
        return repository.findByDeletedFalseOrDeletedIsNull()
            .doOnComplete(() -> log.info("Successfully retrieved all places queries"))
            .doOnError(error -> log.error("Error getting all places queries", error));
    }
    
    /**
     * Obtiene una búsqueda de lugares por ID
     */
    public Mono<GooglePlacesResponse> getPlacesQueryById(String id) {
        log.info("Getting places query by ID: {}", id);
        return repository.findById(id)
            .filter(response -> response.getDeleted() == null || !response.getDeleted())
            .doOnSuccess(response -> {
                if (response != null) {
                    log.info("Successfully retrieved places query with ID: {}", id);
                } else {
                    log.warn("Places query with ID {} not found or is deleted", id);
                }
            })
            .doOnError(error -> log.error("Error getting places query by ID: {}", id, error));
    }
    
    /**
     * Actualiza una búsqueda de lugares con una nueva consulta
     */
    public Mono<GooglePlacesResponse> updatePlacesQuery(String id, String newQuery) {
        log.info("Updating places query with ID: {} with new query: {}", id, newQuery);
        
        return repository.findById(id)
            .filter(response -> response.getDeleted() == null || !response.getDeleted())
            .flatMap(existingResponse -> {
                // Realizar nueva búsqueda con la nueva query
                return searchPlaces(newQuery)
                    .map(newResponse -> {
                        // Mantener el ID original
                        newResponse.setId(id);
                        return newResponse;
                    })
                    .flatMap(repository::save);
            })
            .doOnSuccess(response -> log.info("Successfully updated places query with ID: {}", id))
            .doOnError(error -> log.error("Error updating places query with ID: {}", id, error));
    }
    
    /**
     * Realiza un borrado lógico de una búsqueda de lugares
     */
    public Mono<Void> deletePlacesQuery(String id) {
        log.info("Deleting places query with ID: {}", id);
        
        return repository.findById(id)
            .flatMap(response -> {
                response.setDeleted(true);
                return repository.save(response);
            })
            .then()
            .doOnSuccess(v -> log.info("Successfully deleted places query with ID: {}", id))
            .doOnError(error -> log.error("Error deleting places query with ID: {}", id, error));
    }
}
