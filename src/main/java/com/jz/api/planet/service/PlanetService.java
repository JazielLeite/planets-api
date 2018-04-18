package com.jz.api.planet.service;

import java.util.List;

import javax.validation.Valid;

import com.jz.api.planet.model.Planet;

public interface PlanetService {
	
	/**
	 * Retrieve all current {@link Planet} objects.
	 * @return A list of {@link Planet} objects 
	 */
	public List<Planet> getAll();
	
	/**
	 * search and return a {@link Planet} requested
	 * 
	 * @param id is the identifier of the requested {@link Planet}
	 * @return {@link Planet} object indicated.
	 * 
	 */
	public Planet findById(String planetId);
	
	/**
	 * Search and return a List of {@link Planet} that attend of requested name
	 * 
	 * @param name is the name of the requested {@link Planet}
	 * @return List of {@link Planet} objects indicated.
	 */
	public List<Planet> findByName(String planetName);
	
	/**
	 * Persist a {@link Planet} object
	 * @param planet is the {@link Planet} with a fully valid data
	 */
	public void save(@Valid Planet planet);
	
	/**
	 * Delete a {@link Planet}
	 * @param planetId is the identifier({@link Planet#getId()}) of the {@link Planet} to be excluded 
	 */
	public void delete(String planetId);
	
	/**
	 * Fills in all the empty data of the planet sent with the data of the {@link Planet} referenced by the identifier sent
	 * @param planet is the {@link Planet} with part of the data filled. 
	 * 		  {@link Planet} that will receive data from the planet referenced by the planetId param.
	 * @param planetId is the identifier({@link Planet#getId()}) of the {@link Planet} to be filled
	 */
	public void fill(Planet planet, String planetId);
	
}
