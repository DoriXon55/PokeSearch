package com.project.pokesearch.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.pokesearch.dto.TeamDTO;
import com.project.pokesearch.mapper.UserMapper;
import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.UserRepository;
import com.project.pokesearch.service.TeamService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public TeamController(TeamService teamService, UserMapper userMapper, UserRepository userRepository) {
        this.teamService = teamService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> getUserTeams(Principal principal) {
        User user = getUserFromPrincipal(principal);
        List<Team> teams = teamService.getUserTeams(user);
        List<TeamDTO> teamDTOs = teams.stream()
                .map(userMapper::toTeamDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(teamDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeam(@PathVariable Long id, Principal principal) {
        User user = getUserFromPrincipal(principal);
        Optional<Team> teamOpt = teamService.getTeamById(id)
        .filter(team -> team.getUser().getId().equals(user.getId()));
        
        if (teamOpt.isPresent()) {
            Team team = teamOpt.get();
            return ResponseEntity.ok(userMapper.toTeamDTO(team));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Team not found or you don't have access to it");
        }
    }

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody TeamDTO teamDTO, Principal principal) {
        User user = getUserFromPrincipal(principal);
        if (teamService.existsByNameAndUser(teamDTO.getName(), user)) {
            return ResponseEntity.badRequest().body("You already have a team with this name");
        }
        Team team = new Team();
        team.setName(teamDTO.getName());
        team.setUser(user);
        team.setCreatedAt(LocalDateTime.now());
        Team savedTeam = teamService.createTeam(team);

        return ResponseEntity.ok(userMapper.toTeamDTO(savedTeam));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeam(@PathVariable Long id, @RequestBody TeamDTO teamDTO, Principal principal) {
        User user = getUserFromPrincipal(principal);

        Optional<Team> teamOpt = teamService.getTeamById(id);
        if (teamOpt.isEmpty() || !teamOpt.get().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Team not found or you don't have access");
        }

        Team team = teamOpt.get();

        if (!team.getName().equals(teamDTO.getName()) && teamService.existsByNameAndUser(teamDTO.getName(), user)) {
            return ResponseEntity.badRequest().body("YOu already have a team with this name!");
        }

        team.setName(teamDTO.getName());
        Team updatedTeam = teamService.updateTeam(team);
        return ResponseEntity.ok(userMapper.toTeamDTO(updatedTeam));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id, Principal principal) {
        User user = getUserFromPrincipal(principal);
        return teamService.getTeamById(id)
                .filter(team -> team.getUser().getId().equals(user.getId()))
                .map(team -> {
                    teamService.deleteTeam(team);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    private User getUserFromPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName()).orElseThrow(
                () -> new RuntimeException("User not found"));
    }

}
