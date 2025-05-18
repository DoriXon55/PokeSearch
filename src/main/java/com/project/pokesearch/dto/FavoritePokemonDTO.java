package com.project.pokesearch.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record FavoritePokemonDTO (
        Long id,
        @NotNull(message = "ID pokemona nie może być puste")
        @Positive(message = "ID pokemona musi być liczbą dodatnią")
        Integer pokemonId,
        LocalDateTime addedAt
) {}
