package com.jz.api.planet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import com.jz.api.planet.model.Planet;
import com.jz.api.planet.repository.PlanetRepository;

@Service
public class PlanetServiceImpl implements PlanetService {

	@Autowired
	private PlanetRepository repository;
	
	private final PlanetExtraDataService extraData = new PlanetExtraDataService();
	
	@Override
	@Cacheable(value="galaxy", key="all")
	public List<Planet> getAll() {
		List<Planet> all = repository.findAll();
		all.forEach(p -> p.setParticipations(extraData.getFilmsParticipations(p.getName())));
		return all;
	}

	@Override
	@Cacheable(value="planets", key="#id")
	public Planet findById(String id) {
		Planet planet = repository.findById(id).orElse(null);
		if (planet != null) {
			planet.setParticipations(extraData.getFilmsParticipations(planet.getName()));
		}
		return planet;
	}

	@Override
	@Cacheable(value="galaxy", key="'f'+ #name", unless="#result.size()>10")
	public List<Planet> findByName(String name) {
		List<Planet> result = repository.findByName(name);
		if (result.isEmpty()) {
			return null;
		}
		result.forEach(p -> p.setParticipations(extraData.getFilmsParticipations(p.getName())));
		return result;
	}

	@Override
	@Caching(
			evict = {
					@CacheEvict(value="galaxy", allEntries=true),
					@CacheEvict(value="planets", key="#planetId")
			}
	)
	public void delete(String planetId) {
		repository.deleteById(planetId);
	}

	@Override
	@CachePut(value="planets", key="#planet.id")
	@CacheEvict(value="galaxy", allEntries=true)
	public void save(Planet planet) {
		repository.save(planet);
		planet.setParticipations(extraData.getFilmsParticipations(planet.getName()));
	}

	@Override
	public void fill(Planet part, String planetId) {
		Planet full = findById(planetId);
		if (full == null) {
			throw new IllegalArgumentException("Could not found a Planet with Id " + planetId);
		}
		part.setId(planetId);
		if (part.getName() == null || part.getName().isEmpty()) {
			part.setName(full.getName());
		}
		if (part.getClimate() == null || part.getClimate().isEmpty()) {
			part.setClimate(full.getClimate());
		}
		if (part.getTerrain() == null || part.getTerrain().isEmpty()) {
			part.setTerrain(full.getTerrain());
		}
	}
}
