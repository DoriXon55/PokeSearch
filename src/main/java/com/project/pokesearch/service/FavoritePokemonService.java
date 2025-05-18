package com.project.pokesearch.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.pokesearch.model.FavoritePokemon;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.FavoritePokemonRepository;

@Service
public class FavoritePokemonService {
    
    private final FavoritePokemonRepository favoritePokemonRepository;
    
    @Autowired
    public FavoritePokemonService(FavoritePokemonRepository favoritePokemonRepository) {
        this.favoritePokemonRepository = favoritePokemonRepository;
    }
    
    public List<FavoritePokemon> getUserFavorites(User user) {
        return favoritePokemonRepository.findByUserOrderByAddedAtDesc(user);
    }
    
    public Optional<FavoritePokemon> findByUserAndPokemonId(User user, Integer pokemonId) {
        return favoritePokemonRepository.findByUserAndPokemonId(user, pokemonId);
    }
    
    public boolean existsByUserAndPokemonId(User user, Integer pokemonId) {
        return favoritePokemonRepository.existsByUserAndPokemonId(user, pokemonId);
    }
    
    @Transactional
    public FavoritePokemon addFavoritePokemon(FavoritePokemon favoritePokemon) {
        return favoritePokemonRepository.save(favoritePokemon);
    }
    
    @Transactional
    public void removeFavoritePokemon(FavoritePokemon favoritePokemon) {
        favoritePokemonRepository.delete(favoritePokemon);
    }
    
    @Transactional
    public void removeFavoritePokemonByUserAndPokemonId(User user, Integer pokemonId) {
        Optional<FavoritePokemon> favorite = findByUserAndPokemonId(user, pokemonId);
        favorite.ifPresent(favoritePokemonRepository::delete);
    }
    
    public long countByUser(User user) {
        return favoritePokemonRepository.countByUser(user);
    }
}