package com.pkenterprise.chemflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChemflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChemflowApplication.class, args);
	}

}
