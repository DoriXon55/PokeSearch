package com.project.pokesearch.controller;

import com.project.pokesearch.dto.PokemonGraphQlResponseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.pokesearch.service.PokemonService;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {
    private final PokemonService pokemonService;


    @Autowired
    public PokemonController(PokemonService pokemonService)
    {
        this.pokemonService = pokemonService;
    }

//    @GetMapping
//    public ResponseEntity<?> getPokemonList (@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue="0") int offset)
//    {
//        return ResponseEntity.ok(pokemonService.getPokemonList(limit, offset));
//    }

//    @GetMapping("/search/{nameOrId}")
//    public ResponseEntity<?> searchPokemon(@PathVariable String nameOrId) {
//        return ResponseEntity.ok(pokemonService.searchPokemon(nameOrId));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPokemonDetails(@PathVariable int id) {
        return ResponseEntity.ok(pokemonService.getPokemonDetails(id));
    }
    
    
    
    //---------- GRAPH QL DEV -----------
    
    @GetMapping("/graphql/list")
    public Mono<List<PokemonGraphQlResponseRecord>> getPokemonListGraphQl(@RequestParam(defaultValue = "20") int limit, @RequestParam(defaultValue="0") int offset) {
        return pokemonService.getPokemonListGraphQl(limit, offset);
    }
    
    @GetMapping("/graphql/search/{nameOrId}")
    public Mono<List<PokemonGraphQlResponseRecord>> searchPokemonGraphQl(@PathVariable String nameOrId) {
        return pokemonService.searchPokemonGraphQl(nameOrId);
    }

    @GetMapping("/graphql/{id}")
    public Mono<PokemonGraphQlResponseRecord> getPokemonDetailsGraphQl(@PathVariable int id) {
        return pokemonService.getPokemonDetailsGraphQl(id);
    }
    
    


}
