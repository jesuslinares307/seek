package com.peru.seek.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.peru.seek")
@EntityScan(basePackages = "com.peru.seek.infrastructure.outbound.repositories")
@EnableJpaRepositories(basePackages = "com.peru.seek.infrastructure.outbound.repositories")
public class CandidatesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CandidatesApplication.class, args);
	}

}
