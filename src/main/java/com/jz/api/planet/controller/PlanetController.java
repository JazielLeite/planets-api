package com.jz.api.planet.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jz.api.planet.model.Planet;
import com.jz.api.planet.service.PlanetService;

@RestController
@RequestMapping("/api/v1/planets")
public class PlanetController {
	
	@Autowired
	private PlanetService service;
	
	/**
	 * Retrieve all current {@link Planet} objects.
	 * @return A list of {@link Planet} objects in case of
	 *         success.
	 */
	@GetMapping(produces = "application/json; charset=UTF-8")
	public ResponseEntity<List<Planet>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}
	
	/**
	 * search and display a {@link Planet} requested
	 * 
	 * @param id is the identifier of the requested {@link Planet}
	 * @return {@link Planet} object indicated. 
	 * 		   If no {@link Planet} was found it gives out a HTTP 400 response
	 */
	@GetMapping(path = "/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<Planet> findById(@PathVariable("id") String planetId) {
		
	    return Optional.ofNullable(service.findById(planetId))
	            	   .map(planet -> ResponseEntity.ok(planet) ) // ok 
	            	   .orElseGet( () -> ResponseEntity.notFound().build() ); // not found
	}
	
	/**
	 * search and display a List of {@link Planet}  that attend of requested name
	 * 
	 * @param name is the name of the requested {@link Planet}
	 * @return List of {@link Planet} objects indicated. 
	 * 		   If no {@link Planet} was found it gives out a HTTP 400 response
	 */
	@GetMapping(path = "/find/{name}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<List<Planet>> findByName(@PathVariable("name") String planetName) {
	    return Optional.ofNullable(service.findByName(planetName))
	            	   .map(listPanets -> ResponseEntity.ok(listPanets) ) // ok 
	            	   .orElseGet( () -> ResponseEntity.notFound().build() ); // not found
	}
	
	/**
	 * Persist a {@link Planet}
	 * 
	 * @param planet is the {@link Planet} with a fully valid data
	 * @return the persisted {@link Planet}.
	 */
	@PostMapping(produces = "application/json; charset=UTF-8")
	public ResponseEntity<Planet> save(@RequestBody @Valid Planet planet) {
		try {
			service.save(planet);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(planet);
		}
		return ResponseEntity.ok(planet);
	}
	
	/**
	 * Persist a {@link Planet}
	 * 
	 * @param planet is the {@link Planet} with a fully valid data
	 * @return the persisted {@link Planet}.
	 */
	@PostMapping(path = "/{id}", produces = "application/json; charset=UTF-8")
	public ResponseEntity<Planet> update(@PathVariable("id") String planetId, @RequestBody @Valid Planet planet) {
		planet.setId(planetId);
		try {
			service.save(planet);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(planet);
		}
		return ResponseEntity.ok(planet);
	}

	
	/**
	 * Delete a {@link Planet}
	 * 
	 * @param planetId is the {@link Planet#getId()} 
	 * @return the {@link Planet#getId()} of the deleted {@link Planet}
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") String planetId) {
		service.delete(planetId);
		return ResponseEntity.ok(planetId);
	}

	/**
	 * Part Update {@link Planet}
	 * 
	 * @param is a {@link Planet} with only a part of the data they are wanting to upgrade. This method aims to down the payload
	 * @return the persisted {@link Planet}.
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<String> partUpdate(@PathVariable("id") String planetId, @RequestBody Planet planet) {
		try {	
			service.fill(planet, planetId);
			service.save(planet);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(planet.getId());
		}
		return ResponseEntity.ok(planet.getId());
	}
}
