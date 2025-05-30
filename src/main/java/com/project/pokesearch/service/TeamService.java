package com.project.pokesearch.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.TeamRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository)
    {
        this.teamRepository = teamRepository;
    }

    public List<Team> getUserTeams(User user)
    {
        return teamRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Optional<Team> getTeamById(Long id)
    {
        return teamRepository.findById(id);
    }

    public boolean existsByNameAndUser(String name, User user){
        return teamRepository.existsByNameAndUser(name, user);
    }


    public Team createTeam(Team team)
    {
        return teamRepository.save(team);
    }

    public Team updateTeam(Team team)
    {
        return teamRepository.save(team);
    }

    public void deleteTeam(Team team)
    {
        teamRepository.delete(team);
    }

    
}
