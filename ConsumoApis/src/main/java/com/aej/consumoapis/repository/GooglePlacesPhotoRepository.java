package com.aej.consumoapis.repository;

import com.aej.consumoapis.model.GooglePlacesPhotoResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GooglePlacesPhotoRepository extends ReactiveMongoRepository<GooglePlacesPhotoResponse, String> {
}
