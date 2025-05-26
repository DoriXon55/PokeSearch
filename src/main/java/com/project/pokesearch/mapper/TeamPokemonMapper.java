package com.project.pokesearch.mapper;

import com.project.pokesearch.dto.TeamPokemonDTO;
import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.TeamPokemon;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamPokemonMapper {
    TeamPokemonDTO toDTO(TeamPokemon teamPokemon);
    TeamPokemon toEntity(TeamPokemonDTO teamPokemonDTO);
    
}
