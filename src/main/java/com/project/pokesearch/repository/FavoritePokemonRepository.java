
package com.project.pokesearch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.pokesearch.model.FavoritePokemon;
import com.project.pokesearch.model.User;

@Repository
public interface FavoritePokemonRepository extends JpaRepository<FavoritePokemon, Long> {
    List<FavoritePokemon> findByUser(User user);
    List<FavoritePokemon> findByUserOrderByAddedAtDesc(User user);
    boolean existsByUserAndPokemonId(User user, Integer pokemonId);
    Optional<FavoritePokemon> findByUserAndPokemonId(User user, Integer pokemonId);
    void deleteByUserAndPokemonId(User user, Integer pokemonId);
    long countByUser(User user);
}
