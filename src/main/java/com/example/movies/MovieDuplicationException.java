package com.example.movies;

public class MovieDuplicationException extends Exception {
    public MovieDuplicationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
