package com.aej.consumoapis.repository;

import com.aej.consumoapis.model.GooglePlacesAutocompleteResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GooglePlacesAutocompleteRepository extends ReactiveMongoRepository<GooglePlacesAutocompleteResponse, String> {
}
