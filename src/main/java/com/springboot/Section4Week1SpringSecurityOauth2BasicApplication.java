package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Section4Week1SpringSecurityOauth2BasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(Section4Week1SpringSecurityOauth2BasicApplication.class, args);
	}

}
