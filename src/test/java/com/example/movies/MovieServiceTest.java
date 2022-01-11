package com.example.movies;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MovieServiceTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void getAllMovies() {
        Movie movieSample = new Movie("Blade Runner", 120);
        MovieService movieService = new MovieService(movieRepository);
        movieService.save(movieSample);

        Movie lastMovie = movieService.findAll().get(3);

        assertEquals(movieSample.getTitle(), lastMovie.getTitle());
        assertEquals(movieSample.getDuration(), lastMovie.getDuration());
        assertEquals(movieSample.getID(), lastMovie.getID());
    }

    @Test
    void saveAMovie() {
        MovieService movieService = new MovieService(movieRepository);
        Movie movie = new Movie("Yellowstone", 104);

        movieService.save(movie);
        assertEquals(1.0, movieRepository.count());
    }

    @Test
    void findMovieWithTitle() {
        MovieService movieService = new MovieService(movieRepository);
        Movie avengers = new Movie("Avengers: Age of Ultron", 138);
        Movie yel = new Movie("Yellowstone", 104);
        movieService.save(avengers);
        movieService.save(yel);

        List<Movie> searchResults = movieService.findByTitle("Aven");
        System.out.println("Searching for 'Aven' yields: " + searchResults);
        assertEquals(searchResults.get(0).getTitle(), avengers.getTitle());
    }

    @Test
    void failFindMovieWithTitle() {
        MovieService movieService = new MovieService(movieRepository);
        // 'Cloverfield' not in list
        List<Movie> failedSearchResults = movieService.findByTitle("Cloverfield");
        System.out.println(failedSearchResults);
        assertEquals(failedSearchResults.size(), 0);
    }

    @Test
    void findMovieById() {
        MovieService movieService = new MovieService(movieRepository);
        Movie movie = new Movie("The Matrix", 98);
        Movie movie1 = new Movie("The Matrix Resurrections", 120);
        movieService.save(movie);
        movieService.save(movie1);

        // The Matrix Resurrections is 5 bc at this point, the CommandLineRunner makes 3 movies
        System.out.println(movieService.findByID(5L));
        assertEquals(movie1.getID(), movieService.findByID(5L).getID());
    }

    @Test
    void failFindMovieById() {
        MovieService movieService = new MovieService(movieRepository);
        Movie failedSearch = movieService.findByID(4L);
        assertNull(failedSearch);
    }

    @Test
    void findMovieWithDuration() {
        MovieService movieService = new MovieService(movieRepository);
        Movie m = new Movie("Test for Duration", 499);
        movieService.save(m);

        List<Movie> results = movieService.findByDuration(499);
        System.out.println(results);
        assertEquals(results.get(0).getDuration(), 499);
    }

    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }
}
