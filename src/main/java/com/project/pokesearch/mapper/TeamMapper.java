package com.project.pokesearch.mapper;


import com.project.pokesearch.dto.TeamDTO;
import com.project.pokesearch.dto.TeamPokemonDTO;
import com.project.pokesearch.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {TeamPokemonMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeamMapper {
    @Mapping(source = "pokemons", target = "pokemons")
    TeamDTO toDTO(Team team);

    Team toTeam(TeamDTO teamDTO);
}
