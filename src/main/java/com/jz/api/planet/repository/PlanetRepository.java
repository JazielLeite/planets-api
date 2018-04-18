package com.jz.api.planet.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.jz.api.planet.model.Planet;

public interface PlanetRepository extends MongoRepository<Planet, String> {
	
	
	@Query(value = "{planet.name:?0}")
	List<Planet> findByName(String planetName);

}
