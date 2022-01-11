package com.example.movies;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MoviesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}

	@Bean
	public CommandLineRunner setUp(MovieRepository movieRepository) {
		return (args) -> {
			movieRepository.save(new Movie("The Hateful Eight", 146));
			movieRepository.save(new Movie("Interstellar", 119));
			movieRepository.save(new Movie("The Matrix", 97));
		};
	}
}
