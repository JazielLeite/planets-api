package com.jz.api.planet.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Planet {
	
	@Id
	private String id;
	
	@Indexed
	@NotBlank
	@Size(min=2, max=150)
	private String name;
	
	@NotBlank
	@Size(min=3, max=100)
	private String climate;
	
	@NotBlank
	@Size(min=3, max=200)
	private String terrain;

	@Transient
	private transient int participations;
	
	public Planet() {
		super();
	}

	public Planet(String id, @NotBlank String name, @NotBlank String climate, @NotBlank String terrain) {
		super();
		this.id = id;
		this.name = name;
		this.climate = climate;
		this.terrain = terrain;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the climate
	 */
	public String getClimate() {
		return climate;
	}
	/**
	 * @param climate the climate to set
	 */
	public void setClimate(String climate) {
		this.climate = climate;
	}
	/**
	 * @return the terrain
	 */
	public String getTerrain() {
		return terrain;
	}
	/**
	 * @param terrain the terrain to set
	 */
	public void setTerrain(String terrain) {
		this.terrain = terrain;
	}
	
	/**
	 * @return the participations
	 */
	public int getParticipations() {
		return participations;
	}
	/**
	 * @param participations the participations to set
	 */
	public void setParticipations(int participations) {
		this.participations = participations;
	}
	
	
	
}
