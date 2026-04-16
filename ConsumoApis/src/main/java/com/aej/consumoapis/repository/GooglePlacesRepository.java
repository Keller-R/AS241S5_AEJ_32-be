package com.aej.consumoapis.repository;

import com.aej.consumoapis.model.GooglePlacesResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GooglePlacesRepository extends ReactiveMongoRepository<GooglePlacesResponse, String> {
}
