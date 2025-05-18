package com.project.pokesearch.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.TeamPokemon;
import com.project.pokesearch.repository.TeamPokemonRepository;

@Service
public class TeamPokemonService {
    
    private final TeamPokemonRepository teamPokemonRepository;
    
    @Autowired
    public TeamPokemonService(TeamPokemonRepository teamPokemonRepository) {
        this.teamPokemonRepository = teamPokemonRepository;
    }
    
    public List<TeamPokemon> getTeamPokemons(Team team) {
        return teamPokemonRepository.findByTeamOrderByPosition(team);
    }
    
    public Optional<TeamPokemon> getTeamPokemonById(Long id) {
        return teamPokemonRepository.findById(id);
    }
    
    public Optional<TeamPokemon> getTeamPokemonByTeamAndPosition(Team team, Integer position) {
        return teamPokemonRepository.findByTeamAndPosition(team, position);
    }
    
    @Transactional
    public TeamPokemon addPokemonToTeam(TeamPokemon teamPokemon) {
        Optional<TeamPokemon> existingPokemon = 
            teamPokemonRepository.findByTeamAndPosition(teamPokemon.getTeam(), teamPokemon.getPosition());
        
        existingPokemon.ifPresent(teamPokemonRepository::delete);
        
        return teamPokemonRepository.save(teamPokemon);
    }
    
    @Transactional
    public void removePokemonFromTeam(TeamPokemon teamPokemon) {
        teamPokemonRepository.delete(teamPokemon);
    }
    
    @Transactional
    public void removePokemonFromTeamByPosition(Team team, Integer position) {
        getTeamPokemonByTeamAndPosition(team, position)
            .ifPresent(teamPokemonRepository::delete);
    }
    
    public boolean existsByTeamAndPosition(Team team, Integer position) {
        return teamPokemonRepository.existsByTeamAndPosition(team, position);
    }
}