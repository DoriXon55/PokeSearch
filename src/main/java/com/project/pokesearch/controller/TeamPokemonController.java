package com.project.pokesearch.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.pokesearch.dto.TeamPokemonDTO;
import com.project.pokesearch.mapper.UserMapper;
import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.TeamPokemon;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.UserRepository;
import com.project.pokesearch.service.TeamPokemonService;
import com.project.pokesearch.service.TeamService;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/teams/{teamId}/pokemons")
public class TeamPokemonController {

    private final TeamService teamService;
    private final TeamPokemonService teamPokemonService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public TeamPokemonController(
            TeamService teamService,
            TeamPokemonService teamPokemonService,
            UserMapper userMapper,
            UserRepository userRepository) {
        this.teamService = teamService;
        this.teamPokemonService = teamPokemonService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> getTeamPokemons(@PathVariable Long teamId, Principal principal) {
        User user = getUserFromPrincipal(principal);

        Optional<Team> teamOpt = teamService.getTeamById(teamId);
        if (teamOpt.isEmpty() || !teamOpt.get().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Team not found or you don't have access to it");
        }

        Team team = teamOpt.get();
        List<TeamPokemon> pokemons = teamPokemonService.getTeamPokemons(team);

        return ResponseEntity.ok(pokemons.stream()
                .map(userMapper::toTeamPokemonDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> addPokemonToTeam(
            @PathVariable Long teamId,
            @RequestBody TeamPokemonDTO pokemonDTO,
            Principal principal) {
        User user = getUserFromPrincipal(principal);

        Optional<Team> teamOpt = teamService.getTeamById(teamId);
        if (teamOpt.isEmpty() || !teamOpt.get().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Team not found or you don't have access to it");
        }

        Team team = teamOpt.get();

        if (pokemonDTO.getPosition() < 1 || pokemonDTO.getPosition() > 6) {
            return ResponseEntity.badRequest().body("Position must be between 1 and 6");
        }

        TeamPokemon teamPokemon = new TeamPokemon();
        teamPokemon.setTeam(team);
        teamPokemon.setPokemonId(pokemonDTO.getPokemonId());
        teamPokemon.setPosition(pokemonDTO.getPosition());

        TeamPokemon savedPokemon = teamPokemonService.addPokemonToTeam(teamPokemon);
        return ResponseEntity.ok(userMapper.toTeamPokemonDTO(savedPokemon));
    }

    @DeleteMapping("/{position}")
    public ResponseEntity<?> removePokemonFromTeam(
            @PathVariable Long teamId,
            @PathVariable Integer position,
            Principal principal) {
        User user = getUserFromPrincipal(principal);

        Optional<Team> teamOpt = teamService.getTeamById(teamId);
        if (teamOpt.isEmpty() || !teamOpt.get().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Team not found or you don't have access to it");
        }

        Team team = teamOpt.get();

        if (!teamPokemonService.existsByTeamAndPosition(team, position)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No pokemon found at this position");
        }

        teamPokemonService.removePokemonFromTeamByPosition(team, position);
        return ResponseEntity.ok("Pokemon removed successfully");
    }

    private User getUserFromPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}