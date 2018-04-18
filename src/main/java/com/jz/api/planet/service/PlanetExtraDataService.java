package com.jz.api.planet.service;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jz.api.planet.PlanetsApiApplication;

/**
 * This class was built for the purpose of consulting the Star Wars API 
 * and collecting information about the planets - how often each planet appears during the series of movies 
 * for more information on the API see: https://swapi.co/documentation
 * 
 * @author jaziel
 *
 */
public class PlanetExtraDataService {

	private static final String STARTWARS_API_URL = "https://swapi.co/api/planets/";
	private static final long REFRESH_TIME = TimeUnit.MINUTES.toMillis(15); // 15 minutes in millis

	private static final Logger log = LoggerFactory.getLogger(PlanetsApiApplication.class);
	
	private Map<String, Integer> participationsLib;
	
	public PlanetExtraDataService() {
		this.participationsLib = getExtraData();
		new Timer("star.war.api").scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					update();
				} catch (Exception e) {
					log.error("Error when refresh extra data: " + e.getMessage(), e);
				}
			}
		}, REFRESH_TIME, REFRESH_TIME);
	}
	
	public void update() {
		log.debug("Refreshing PlanetExtraData # participationsLib");
		this.participationsLib = getExtraData();
		log.debug("PlanetExtraData # participationsLib updated!");
	}
	
	public int getFilmsParticipations(String planetName) {
		return participationsLib.getOrDefault(planetName, 0);
	}
	
	private Map<String, Integer> getExtraData() {
		Map<String, Integer> participations = new HashMap<>();
		
    	String nextPage = STARTWARS_API_URL;
    	while (nextPage != null && !nextPage.isEmpty()) {
    		
    		JSONObject page;
			try {
				page = new JSONObject(getData(nextPage));
			} catch (Exception e) {
				e.printStackTrace();
				return Collections.emptyMap();
    		}
    		nextPage = page.optString("next");
    		JSONArray results = page.optJSONArray("results");
    		if (results == null) {
    			continue;
    		}
    		for (int p = 0; p < results.length(); p++) {
				loadExtraData(participations, results.optJSONObject(p));
    		}
    	}
    	return participations;
    }
	
	private void loadExtraData(Map<String, Integer> map, JSONObject planet) {
		if (planet == null) {
			return;
		}
		String name = planet.optString("name");
		if (name == null) {
			return;
		}
		JSONArray films = planet.optJSONArray("films");
		if (films == null || films.length() <= 0) {
			return;
		}
		log.info("Load info from [Planet: " + name + ", Participations: " + films.length() + "]");
		map.put(name, films.length());
	}
	
	private static String getData(String input) throws Exception {
		log.debug("GetData from url: " + input);
        URL url;
		try {
			url = new URL(input);
		} catch (MalformedURLException e) {
			return new JSONObject().toString();
		}
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("User-Agent", "java-test");
        connection.setDoInput(true);
        int responseCode = connection.getResponseCode();
		if (responseCode != HttpServletResponse.SC_OK) {
        	throw new IllegalStateException("Response not OK for URL: " + input + " - " + responseCode);
        }
        DataInputStream in = new DataInputStream(connection.getInputStream());
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuffer result = new StringBuffer();
        while ((line = buffer.readLine()) != null) {
            result.append(line);
            result.append('\r');
        }
        return result.toString();
    }

	
}
