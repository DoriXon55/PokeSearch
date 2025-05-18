package com.project.pokesearch.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.project.pokesearch.dto.FavoritePokemonDTO;
import com.project.pokesearch.dto.TeamDTO;
import com.project.pokesearch.dto.TeamPokemonDTO;
import com.project.pokesearch.dto.UserDTO;
import com.project.pokesearch.model.FavoritePokemon;
import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.TeamPokemon;
import com.project.pokesearch.model.User;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) return null;

        List<FavoritePokemonDTO> favoritePokemonDTOs = null;
        if (user.getFavoritePokemons() != null) {
            favoritePokemonDTOs = user.getFavoritePokemons().stream()
                    .map(this::toFavoritePokemonDTO)
                    .collect(Collectors.toList());
        }

        List<TeamDTO> teamDTOs = null;
        if (user.getTeams() != null) {
            teamDTOs = user.getTeams().stream()
                    .map(this::toTeamDTO)
                    .collect(Collectors.toList());
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getLastPasswordChangeDate(),
                favoritePokemonDTOs,
                teamDTOs
        );
    }

    public FavoritePokemonDTO toFavoritePokemonDTO(FavoritePokemon favoritePokemon) {
        if (favoritePokemon == null) return null;

        return new FavoritePokemonDTO(
                favoritePokemon.getId(),
                favoritePokemon.getPokemonId(),
                favoritePokemon.getAddedAt()
        );
    }

    public TeamDTO toTeamDTO(Team team) {
        if (team == null) return null;

        List<TeamPokemonDTO> pokemonDTOs = null;
        if (team.getPokemons() != null) {
            pokemonDTOs = team.getPokemons().stream()
                    .map(this::toTeamPokemonDTO)
                    .collect(Collectors.toList());
        }

        return new TeamDTO(
                team.getId(),
                team.getName(),
                team.getCreatedAt(),
                pokemonDTOs
        );
    }


    public TeamPokemonDTO toTeamPokemonDTO(TeamPokemon teamPokemon) {
        if (teamPokemon == null) return null;

        return new TeamPokemonDTO(
                teamPokemon.getId(),
                teamPokemon.getPokemonId(),
                teamPokemon.getPosition()
        );
    }



}
