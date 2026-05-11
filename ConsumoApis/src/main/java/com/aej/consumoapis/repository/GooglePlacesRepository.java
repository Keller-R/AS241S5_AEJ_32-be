package com.aej.consumoapis.repository;

import com.aej.consumoapis.model.GooglePlacesResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GooglePlacesRepository extends ReactiveMongoRepository<GooglePlacesResponse, String> {
    
    /**
     * Encuentra todos los registros que no han sido eliminados lógicamente
     */
    Flux<GooglePlacesResponse> findByDeletedFalseOrDeletedIsNull();
    
    /**
     * Encuentra un registro por ID que no ha sido eliminado
     */
    Mono<GooglePlacesResponse> findByIdAndDeletedFalse(String id);
}
