package com.aej.consumoapis.repository;

import com.aej.consumoapis.model.IpGeoLocationResponse;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpGeoLocationRepository extends ReactiveMongoRepository<IpGeoLocationResponse, String> {
}
