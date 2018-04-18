package com.jz.api.planet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.doAnswer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.jz.api.planet.model.Planet;
import com.jz.api.planet.repository.PlanetRepository;
import com.jz.api.planet.service.PlanetService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PlanetServiceTests {

	@MockBean
	private PlanetRepository repository;
	
	@Autowired
	private PlanetService service; 
	
	@Test
	public void list() {
		List<Planet> all = Arrays.asList(
				new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests"), 
				new Planet("5ad6518f95bf894f3d13120b", "PlanetTest-2", "arid", "desert"), 
				new Planet("5ad651a095bf894f3d13120c", "PlanetTest-3", "artificial temperate", "airless asteroid") 
		);
		BDDMockito.when(repository.findAll()).thenReturn(all);
		assertSame(service.getAll().size(), 3);
	}
	
	@Test
	public void find() {
		BDDMockito.when(repository.findById("5ad63d4995bf894cc0fe78f9")).thenReturn(Optional.of(new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests")));
		assertSame(service.findById("5ad63d4995bf894cc0fe78f9").getName(), "PlanetTest-1");
	}
	
	@Test
	public void findByName() {
		BDDMockito.when(repository.findByName("PlanetTest-1")).thenReturn(Arrays.asList(new Planet("5ad63d4995bf894cc0fe78f9", "PlanetTest-1", "temperate, tropical", "jungle, rainforests")));
		List<Planet> result = service.findByName("PlanetTest-1");
		assertSame(result.size(), 1);
		assertSame(result.get(0).getId(), "5ad63d4995bf894cc0fe78f9");
	}
	
	@Test
	public void save() { 
		Planet p = new Planet(null, "Tatooine", "arid", "desert");
	    doAnswer(new Answer() {
	    	@Override
	        public Void answer(InvocationOnMock invocation) throws Throwable {
	            Object[] arguments = invocation.getArguments();
	            if (arguments != null && arguments.length == 1 && arguments[0] != null) {
	            	Planet pa = (Planet)arguments[0];
	                pa.setId("5ad63d4995bf894cc0fe78f9");
	            }
	            return null;
	    	}
	    }).when(repository).save(p);
	    service.save(p);
		assertNotEquals(p.getId(), null);
		assertSame(p.getParticipations(), 5);
	}

	@Test
	public void delete() { 
		String id_to_delete = "5ad63d4995bf894cc0fe78f9";
		final StringBuilder deleted_id = new StringBuilder();
		doAnswer(new Answer() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] arguments = invocation.getArguments();
				if (arguments != null && arguments.length == 1 && arguments[0] != null) {
					deleted_id.append(arguments[0]);
				}
				return null;
			}
		}).when(repository).deleteById(id_to_delete);
		service.delete(id_to_delete);
		assertEquals(id_to_delete, deleted_id.toString());
	}

}
