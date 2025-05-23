package com.project.pokesearch.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.cache.annotation.Cacheable;

@Service
public class PokemonService {
    private static final String POKE_API_URL = "https://pokeapi.co/api/v2/";
    private final RestTemplate restTemplate;

    @Autowired
    public PokemonService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "pokemonList", key = "'list-' + #limit + '-' + #offset")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPokemonList(int limit, int offset)
    {
        String url = POKE_API_URL + "pokemon?limit=" + limit + "&offset=" + offset;
        return restTemplate.getForObject(url, Map.class);
    }

    @Cacheable(value = "pokemonSearch", key = "#nameOrId.toLowerCase()")
    @SuppressWarnings("unchecked")
    public Map<String, Object> searchPokemon(String nameOrId) {
        String url = POKE_API_URL + "pokemon/" + nameOrId.toLowerCase();
        try {
            return restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound ex) {
            // Return a structured error response instead of all Pokemon
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Pokemon not found");
            errorResponse.put("message", "No Pokemon found with name or ID: " + nameOrId);
            return errorResponse;
        }
    }

    @Cacheable(value = "pokemonDetails", key="#id")
    @SuppressWarnings("unchecked")
    public Map<String, Object> getPokemonDetails(int id)
    {
        String url = POKE_API_URL + "pokemon/" + id;
        return restTemplate.getForObject(url, Map.class);

    }

    
}
