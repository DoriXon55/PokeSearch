package com.project.pokesearch.mapper;


import com.project.pokesearch.dto.FavoritePokemonDTO;
import com.project.pokesearch.model.FavoritePokemon;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoritePokemonMapper {
    FavoritePokemonDTO toDTO(FavoritePokemon favoritePokemon);
    FavoritePokemon toEntity(FavoritePokemonDTO favoritePokemonDTO);
}
