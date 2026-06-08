package com.example.propietario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PropietarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropietarioApplication.class, args);
	}

}
