package com.aej.consumoapis.repository;

import com.aej.consumoapis.model.IpGeoLocationResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IpGeoLocationRepository extends ReactiveMongoRepository<IpGeoLocationResponse, String> {
    
    /**
     * Encuentra todos los registros que no han sido eliminados lógicamente
     */
    Flux<IpGeoLocationResponse> findByDeletedFalseOrDeletedIsNull();
    
    /**
     * Encuentra un registro por ID que no ha sido eliminado
     */
    Mono<IpGeoLocationResponse> findByIdAndDeletedFalse(String id);
}
