package com.jz.api.planet.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Size;

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
	
	@GetMapping
	public ResponseEntity<List<Planet>> getAll() {
		return ResponseEntity.ok(service.getAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Planet> findById(@PathVariable("id") String planetId) {
		
	    return Optional.ofNullable(service.findById(planetId))
	            	   .map(planet -> ResponseEntity.ok(planet) ) // ok 
	            	   .orElseGet( () -> ResponseEntity.notFound().build() ); // not found
	}
	
	@GetMapping("/find/{name}")
	public ResponseEntity<List<Planet>> findByName(@PathVariable("name") String planetName) {
	    return Optional.ofNullable(service.findByName(planetName))
	            	   .map(listPanets -> ResponseEntity.ok(listPanets) ) // ok 
	            	   .orElseGet( () -> ResponseEntity.notFound().build() ); // not found
	}
	
	@PostMapping
	public ResponseEntity<Planet> save(@RequestBody @Valid Planet planet) {
		try {
			service.save(planet);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(planet);
		}
		return ResponseEntity.ok(planet);
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<Planet> update(@PathVariable("id") String planetId, @RequestBody @Valid Planet planet) {
		planet.setId(planetId);
		try {
			service.save(planet);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(planet);
		}
		return ResponseEntity.ok(planet);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable("id") String planetId) {
		service.delete(planetId);
		return ResponseEntity.ok(planetId);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<String> partUpdate(@PathVariable("id") @Size(min=2, max=150) String planetId, @RequestBody Planet planet) {
		try {	
			service.fill(planet, planetId);
			service.save(planet);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(planet.getId());
		}
		return ResponseEntity.ok(planet.getId());
	}
}
