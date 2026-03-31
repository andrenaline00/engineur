package com.special.specializationtools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.special")
@EntityScan(basePackages = "com.special.domain")
@EnableJpaRepositories(basePackages = "com.special.repositories")
public class SpecializationToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpecializationToolsApplication.class, args);
	}

}
