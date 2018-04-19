package com.jz.api.planet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.jz.api.planet.controller.PlanetController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration, used to enable swagger documentation for Spring.
 */
@Configuration
@ComponentScan(basePackageClasses=PlanetController.class)
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * @return {@link Docket} object providing default swagger interface for the
	 *         Planets API endpoints.
	 */
	@Bean
	public Docket planetsApiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.pathMapping("/")
				.select()
				.paths(PathSelectors.regex("/api/.*"))
				.build();		
	}
	
}
