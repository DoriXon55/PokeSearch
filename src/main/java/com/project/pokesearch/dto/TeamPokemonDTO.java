package com.project.pokesearch.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TeamPokemonDTO(
        Long id,

        @NotNull(message = "Pokemon ID cannot be empty")
        Integer pokemonId,

        @NotNull(message = "Position cannot be empty")
        @Min(value = 1, message = "Position must be a number between 1 and 6")
        @Max(value = 6, message = "Position must be a number between 1 and 6")
        Integer position
) {}