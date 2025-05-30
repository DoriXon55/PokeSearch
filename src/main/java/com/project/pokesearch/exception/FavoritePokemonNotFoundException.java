package com.project.pokesearch.exception;

public class FavoritePokemonNotFoundException extends RuntimeException {
    public FavoritePokemonNotFoundException(String message) {
        super(message);
    }
}
