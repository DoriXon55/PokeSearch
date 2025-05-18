package com.project.pokesearch.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamDTO(
        Long id,

        @NotBlank(message = "Nazwa druzyny nie moze byc pusta")
        @Size(min = 2, max = 50, message = "Nazwa druzyny musi miec od 2 do 50 znakow")
        String name,

        LocalDateTime createdAt,

        @Size(max = 6, message = "Druzyna moze miec maksymalnie 6 pokemonow")
        List<TeamPokemonDTO> pokemons
) {}