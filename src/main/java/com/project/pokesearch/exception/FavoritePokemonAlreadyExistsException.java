package com.project.pokesearch.exception;

public class FavoritePokemonAlreadyExistsException extends RuntimeException{
    public FavoritePokemonAlreadyExistsException(String message) {
        super(message);
    }

}
