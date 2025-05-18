package com.project.pokesearch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.pokesearch.model.Team;
import com.project.pokesearch.model.User;


@Repository
public interface  TeamRepository extends JpaRepository<Team, Long>{
    List<Team> findByUser(User user);
    List<Team> findByUserOrderByCreatedAtDesc(User user);
    Team findByNameAndUser(String name, User user);
    boolean existsByNameAndUser(String name, User user);
}
