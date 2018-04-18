package com.jz.api.planet.service;

import java.util.List;

import com.jz.api.planet.model.Planet;

public interface PlanetService {
	
	
	public List<Planet> getAll();
	
	public Planet findById(String planetId);
	
	public List<Planet> findByName(String planetName);
	
	public void save(Planet planet);
	
	public void delete(String planetId);
	
	public void fill(Planet part, String planetId);
	
}
