package com.example.movies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MoviesController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/movies")
    ResponseEntity<List<Movie>> getAllMovies() {
        return new ResponseEntity<>(movieService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/movies/{id}")
    ResponseEntity<Movie> getMovieByID(@PathVariable("id") long id) throws ResponseStatusException {
        Movie result = movieService.findByID(id);
        if (result == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Movie with ID [%s] not found.", id));
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/movies")
    ResponseEntity<Movie> create(@RequestBody Movie movie) {
        return new ResponseEntity<>(movieService.save(movie), HttpStatus.CREATED);
    }

    @GetMapping("/movies/search")
    ResponseEntity<List<Movie>> getEmptySearch() {
        System.out.println("*********");
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @GetMapping("/movies/search")
    // TODO: mapping ambiguous -> actually implement the searching functionality
    ResponseEntity<List<Movie>> findByTitle(@RequestParam("title") String title) {
        List<Movie> results = movieService.findByTitle(title);
        if (results.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No movies found starting with '" + title + "'."));
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/movies/search?duration={duration}")
    ResponseEntity<List<Movie>> findByDuration(@PathVariable("duration") int duration) {
        List<Movie> results = movieService.findByDuration(duration);
        if (results.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No movies found with [" + duration + " mins] length."));
        }
        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
