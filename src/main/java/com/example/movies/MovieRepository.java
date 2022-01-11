package com.example.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("movieRepository")
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findById(long id);

    @Query("select m from Movie m where m.title like %?1%")
    List<Movie> findByTitle(String title);

    List<Movie> findByDuration(int duration);
}
