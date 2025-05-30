package com.project.pokesearch.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.project.pokesearch.mapper.FavoritePokemonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.pokesearch.dto.FavoritePokemonDTO;
import com.project.pokesearch.model.FavoritePokemon;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.UserRepository;
import com.project.pokesearch.service.FavoritePokemonService;

@RestController
@RequestMapping("/api/favorite-pokemons")
public class FavoritePokemonController {
    
    private final FavoritePokemonService favoritePokemonService;
    private final UserRepository userRepository;
    private final FavoritePokemonMapper favoritePokemonMapper;
    
    @Autowired
    public FavoritePokemonController(
            FavoritePokemonService favoritePokemonService,
            UserRepository userRepository,
            FavoritePokemonMapper favoritePokemonMapper) {
        this.favoritePokemonService = favoritePokemonService;
        this.userRepository = userRepository;
        this.favoritePokemonMapper = favoritePokemonMapper;
    }
    
    @GetMapping
    public ResponseEntity<?> getUserFavorites(Principal principal) {
        User user = getUserFromPrincipal(principal);
        List<FavoritePokemon> favorites = favoritePokemonService.getUserFavorites(user);
        List<FavoritePokemonDTO> favoriteDTOs = favorites.stream()
                .map(favoritePokemonMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(favoriteDTOs);
    }
    
    @GetMapping("/{pokemonId}")
    public ResponseEntity<?> checkIsFavorite(@PathVariable Integer pokemonId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        boolean isFavorite = favoritePokemonService.existsByUserAndPokemonId(user, pokemonId);
        return ResponseEntity.ok(isFavorite);
    }
    
    @PostMapping
    public ResponseEntity<?> addFavoritePokemon(@RequestBody FavoritePokemonDTO favoriteDTO, Principal principal) {
        User user = getUserFromPrincipal(principal);
        
        if (favoritePokemonService.existsByUserAndPokemonId(user, favoriteDTO.pokemonId())) {
            return ResponseEntity.badRequest().body("Pokemon already in favorites");
        }
        
        FavoritePokemon favoritePokemon = new FavoritePokemon();
        favoritePokemon.setUser(user);
        favoritePokemon.setPokemonId(favoriteDTO.pokemonId());
        favoritePokemon.setAddedAt(LocalDateTime.now());
        
        FavoritePokemon saved = favoritePokemonService.addFavoritePokemon(favoritePokemon);
        return ResponseEntity.ok(favoritePokemonMapper.toDTO(saved));
    }
    
    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<?> removeFavoritePokemon(@PathVariable Integer pokemonId, Principal principal) {
        User user = getUserFromPrincipal(principal);
        
        if (!favoritePokemonService.existsByUserAndPokemonId(user, pokemonId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokemon not found in favorites");
        }
        
        favoritePokemonService.removeFavoritePokemonByUserAndPokemonId(user, pokemonId);
        return ResponseEntity.ok("Pokemon removed from favorites");
    }
    
    private User getUserFromPrincipal(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}