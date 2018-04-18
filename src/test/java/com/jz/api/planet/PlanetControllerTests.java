package com.jz.api.planet;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.jz.api.planet.model.Planet;
import com.jz.api.planet.repository.PlanetRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanetControllerTests {

	public static final String API_BASE_URL = "/api/v1/planets/";
	
	@MockBean
	private PlanetRepository repository;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@TestConfiguration
	static class Config {
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder();
		}
	}
	
	@Test
	public void list() {
		ResponseEntity<Planet[]> resp = restTemplate.getForEntity(API_BASE_URL, Planet[].class);
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void find() {
		BDDMockito.when(repository.findById("5ad63d4995bf894cc0fe78f9")).thenReturn(Optional.of(new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests")));
		ResponseEntity<Planet> resp = restTemplate.getForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", Planet.class);
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
		assertEquals(resp.getBody().getName(), "PlanetTest-1");
	}
	
	@Test
	public void notFound() {
		ResponseEntity<Planet> resp = restTemplate.getForEntity(API_BASE_URL + "invalid-planet-id", Planet.class);
		assertEquals(resp.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void findByName() {
		BDDMockito.when(repository.findByName("PlanetTest-1")).thenReturn(Arrays.asList(new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests")));
		ResponseEntity<Planet[]> resp = restTemplate.getForEntity(API_BASE_URL + "find/PlanetTest-1", Planet[].class);
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
		assertEquals(resp.getBody()[0].getId(), "5ad63d4995bf894cc0fe78f9");
	}
	
	@Test
	public void notFoundByName() {
		ResponseEntity<Planet[]> resp = restTemplate.getForEntity(API_BASE_URL + "find/invalid-planet-name", Planet[].class);
		assertEquals(resp.getStatusCode(), HttpStatus.NOT_FOUND);
	}

	@Test
	public void save() {
		Planet p = new Planet(null, "PlanetTest-1", "temperate, tropical", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL, p, Planet.class);
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void notValidSave_without_name() {
		Planet p = new Planet(null, null, "temperate, tropical", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL, p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidSave_without_climate() {
		Planet p = new Planet(null, "PlanetTest-1", null, "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL, p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidSave_with_blank_terrain() {
		Planet p = new Planet(null, "PlanetTest-1", "temperate, tropical", "");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL, p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}	
	@Test
	public void notValidSave_with_blank_name() {
		Planet p = new Planet(null, "", "temperate, tropical", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL, p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidSave_with_blank_climate() {
		Planet p = new Planet(null, "PlanetTest-1", "", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL, p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidSave_without_terrain() {
		Planet p = new Planet(null, "PlanetTest-1", "temperate, tropical", null);
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL, p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void update() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
		assertEquals(resp.getBody().getName(), "PlanetTest-1");
	}
	
	@Test
	public void notValidUpdate_without_name() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", null, "temperate, tropical", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidUpdate_without_climate() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", null, "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidUpdate_without_terrain() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", null);
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidUpdate_with_small_name() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "P", "temperate, tropical", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidUpdate_with_small_climate() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "CL", "jungle, rainforests");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidUpdate_with_small_terrain() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "TR");
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}

	@Test
	public void notValidUpdate_with_big_terrain() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests");
		while (p.getTerrain().length() < 200) {
			p.setTerrain(p.getTerrain() + ", jungle, rainforests");
		}
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidUpdate_with_big_climate() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests");
		while (p.getClimate().length() < 100) {
			p.setClimate(p.getClimate() + ", temperate, tropical");
		}
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void notValidUpdate_with_big_name() {
		Planet p = new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests");
		while (p.getName().length() < 150) {
			p.setName(p.getName() + " - PlanetTest-1");
		}
		ResponseEntity<Planet> resp = restTemplate.postForEntity(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", p, Planet.class);
		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
	}
	
	@Test
	public void delete() {
		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.DELETE, null, String.class);
		assertEquals(resp.getStatusCode(), HttpStatus.OK);
		assertEquals(resp.getBody(), "5ad63d4995bf894cc0fe78f9");
	}

	
//	@Test
//	public void partUpdate() {
//		Planet p = new Planet(null, "PlanetTest-1", null, null);
//		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.PATCH, new HttpEntity<Planet>(p), String.class);
//		assertEquals(resp.getStatusCode(), HttpStatus.OK);
//		assertEquals(resp.getBody(), "5ad63d4995bf894cc0fe78f9");
//	}
//	
//	
//	@Test
//	public void notValid_partUpdate_with_small_name() {
//		Planet p = new Planet(null, "P", null, null);
//		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.PATCH, new HttpEntity<Planet>(p), String.class);
//		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
//	}
//	
//	@Test
//	public void notValid_partUpdate_with_small_climate() {
//		Planet p = new Planet(null, null, "CL", null);
//		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.PATCH, new HttpEntity<Planet>(p), String.class);
//		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
//	}
//	
//	@Test
//	public void notValid_partUpdate_with_small_terrain() {
//		Planet p = new Planet(null, null, null, "TR");
//		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.PATCH, new HttpEntity<Planet>(p), String.class);
//		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
//	}
//
//	@Test
//	public void notValid_partUpdate_with_big_terrain() {
//		Planet p = new Planet(null, null, null, "jungle, rainforests");
//		while (p.getTerrain().length() < 200) {
//			p.setTerrain(p.getTerrain() + ", jungle, rainforests");
//		}
//		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.PATCH, new HttpEntity<Planet>(p), String.class);
//		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
//	}
//	
//	@Test
//	public void notValid_partUpdate_with_big_climate() {
//		Planet p = new Planet(null, null, "temperate, tropical", null);
//		while (p.getClimate().length() < 100) {
//			p.setClimate(p.getClimate() + ", temperate, tropical");
//		}
//		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.PATCH, new HttpEntity<Planet>(p), String.class);
//		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
//	}
//	
//	@Test
//	public void notValid_partUpdate_with_big_name() {
//		Planet p = new Planet(null, "PlanetTest-1", null, null);
//		while (p.getName().length() < 150) {
//			p.setName(p.getName() + " - PlanetTest-1");
//		}
//		ResponseEntity<String> resp = restTemplate.exchange(API_BASE_URL + "5ad63d4995bf894cc0fe78f9", HttpMethod.PATCH, new HttpEntity<Planet>(p), String.class);
//		assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
//	}
	
}
