package com.example.movies;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class MoviesControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    void getAllMovies() throws Exception {
        List<Movie> moviesList = new ArrayList<Movie>();
        moviesList.add(new Movie(1L, "Blade Runner", 120));
        moviesList.add(new Movie(2L, "Avengers", 80));
        when(movieService.findAll()).thenReturn(moviesList);

        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(jsonPath("$", hasSize(2))).andDo(print());
    }

    @Test
    void successfullyAddAMovie() throws Exception {
        Movie frailty = new Movie("Frailty", 89);
        when(movieService.save(any(Movie.class))).thenReturn(frailty);

        ObjectMapper objectMapper = new ObjectMapper();
        String frailtyJSON = objectMapper.writeValueAsString(frailty);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(frailtyJSON)
        );
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Frailty"))
                .andExpect(jsonPath("$.duration").value(89));
    }

    @Test
    void getMovieByID() throws Exception {
        List<Movie> moviesList = new ArrayList<>();
        Movie hulk = new Movie(1L, "The Incredible Hulk", 125);
        moviesList.add(hulk);

        ObjectMapper objectMapper = new ObjectMapper();
        String hulkJSON = objectMapper.writeValueAsString(hulk);
        when(movieService.findByID(1L)).thenReturn(hulk);

        // POST Hulk movie with id 1
        mockMvc.perform(MockMvcRequestBuilders
                .post("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(hulkJSON));
        // GET Hulk movie by searching for id 1
        mockMvc.perform(MockMvcRequestBuilders
                .get("/movies/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.title").value("The Incredible Hulk"));
    }

    @Test
    void filterBladeRunnerMovies() throws Exception {
        Movie br = new Movie(1L, "Blade Runner", 115);
        Movie br2049 = new Movie(2L, "Blade Runner 2049", 123);
        Movie avengers = new Movie(3L, "The Avengers", 97);

        List<Movie> movies = new ArrayList<>();
        movies.add(br); movies.add(br2049); movies.add(avengers);
        List<String> moviesToJSON = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        // convert each movie to JSON string
        for(Movie movie : movies) {
            String movieJSON = objectMapper.writeValueAsString(movie);
            moviesToJSON.add(movieJSON);
        }

        when(movieService.findByTitle("Blade")).thenReturn(Arrays.asList(br, br2049));
        // POST each new movie
        for(int i = 0; i < 3; i++) {
            mockMvc.perform(MockMvcRequestBuilders
                    .post("/movies")
                    .content(moviesToJSON.get(i))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }
        // searching for 'Blade Runner 2049' should yield only that movie
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .get("/movies/search/{title}", "Blade Runner 2049")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1))).andDo(print());
    }
}
