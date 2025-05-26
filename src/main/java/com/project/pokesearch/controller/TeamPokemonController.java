package com.project.pokesearch.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import com.project.pokesearch.exception.TeamNotFoundOrAccessException;
import com.project.pokesearch.mapper.TeamPokemonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.pokesearch.dto.TeamPokemonDTO;
import com.project.pokesearch.mapper.UserMapper1;
import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.TeamPokemon;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.UserRepository;
import com.project.pokesearch.service.TeamPokemonService;
import com.project.pokesearch.service.TeamService;

@RestController
@RequestMapping("/api/teams/{teamId}/pokemons")
public class TeamPokemonController {

    private final TeamService teamService;
    private final TeamPokemonService teamPokemonService;
    private final TeamPokemonMapper teamPokemonMapper;
    private final UserRepository userRepository;

    @Autowired
    public TeamPokemonController(
            TeamService teamService,
            TeamPokemonService teamPokemonService,
            TeamPokemonMapper teamPokemonMapper,
            UserRepository userRepository) {
        this.teamService = teamService;
        this.teamPokemonService = teamPokemonService;
        this.teamPokemonMapper = teamPokemonMapper;
        this.userRepository = userRepository;
    }



    @GetMapping
    public ResponseEntity<?> getTeamPokemons(@PathVariable Long teamId, Principal principal) {
        Team team = getTeamAndVerifyAccess(teamId, principal);
        List<TeamPokemon> pokemons = teamPokemonService.getTeamPokemons(team);

        return ResponseEntity.ok(pokemons.stream()
                .map(teamPokemonMapper::toDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> addPokemonToTeam(
            @PathVariable Long teamId,
            @RequestBody TeamPokemonDTO pokemonDTO,
            Principal principal) {
        Team team = getTeamAndVerifyAccess(teamId, principal);

        if (pokemonDTO.position() < 1 || pokemonDTO.position() > 6) {
            return ResponseEntity.badRequest().body("Position must be between 1 and 6");
        }

        TeamPokemon teamPokemon = new TeamPokemon();
        teamPokemon.setTeam(team);
        teamPokemon.setPokemonId(pokemonDTO.pokemonId());
        teamPokemon.setPosition(pokemonDTO.position());

        TeamPokemon savedPokemon = teamPokemonService.addPokemonToTeam(teamPokemon);
        return ResponseEntity.ok(teamPokemonMapper.toDTO(savedPokemon));
    }

    @DeleteMapping("/{position}")
    public ResponseEntity<?> removePokemonFromTeam(
            @PathVariable Long teamId,
            @PathVariable Integer position,
            Principal principal) {
        Team team = getTeamAndVerifyAccess(teamId, principal);

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

    private Team getTeamAndVerifyAccess(Long teamId, Principal principal)
    {
        User user = getUserFromPrincipal(principal);
        return teamService.getTeamById(teamId).filter(team -> team.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new TeamNotFoundOrAccessException("Team not found or you don't have access to it"));
    }
}