package com.project.pokesearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.TeamPokemon;

@Repository
public interface TeamPokemonRepository extends JpaRepository<TeamPokemon, Long> {
    List<TeamPokemon> findByTeam(Team team);
    List<TeamPokemon> findByTeamOrderByPosition(Team team);
    Optional<TeamPokemon> findByTeamAndPosition(Team team, Integer position);
    boolean existsByTeamAndPokemonId(Team team, Integer pokemonId);
    long countByTeam(Team team);
    boolean existsByTeamAndPosition(Team team, Integer position);
}
