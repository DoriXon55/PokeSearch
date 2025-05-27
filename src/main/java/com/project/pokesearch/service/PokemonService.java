package com.project.pokesearch.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.pokesearch.dto.PokemonGraphQlResponseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.springframework.cache.annotation.Cacheable;
import reactor.core.publisher.Mono;

@Service
public class PokemonService {
    
    
    private static final String POKE_API_URL = "https://pokeapi.co/api/v2/";
    
    private final RestTemplate restTemplate;
    private final HttpGraphQlClient graphQlClient;

    @Autowired
    public PokemonService(HttpGraphQlClient graphQlClient, RestTemplate restTemplate)
    {
        this.graphQlClient = graphQlClient;
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
    
    
    // ------------- GRAPHQL PART DEV --------------------
    @Cacheable(value = "pokemonListGraphQl", key = "'list-gql-' + #limit + '-' + #offset")
    public Mono<List<PokemonGraphQlResponseRecord>> getPokemonListGraphQl(int limit, int offset){
        return graphQlClient.documentName("pokemonList")
                .variable("limit", limit)
                .variable("offset", offset)
                .execute()
                .map(response -> response.field("pokemon_v2_pokemon").toEntityList(PokemonGraphQlResponseRecord.class));
    }

    @Cacheable(value = "pokemonSearchGraphQl", key = "'search-gql-' + #nameOrId.toLowerCase()")
    public Mono<List<PokemonGraphQlResponseRecord>> searchPokemonGraphQl(String nameOrId){
        Map<String, Object> variables = new HashMap<>();
        Map<String, Object> whereCondition = new HashMap<>();
        Map<String, Object> filter = new HashMap<>();

        try{
            int id = Integer.parseInt(nameOrId);
            filter.put("_eq", id);
            whereCondition.put("id", filter);
        } catch (NumberFormatException e)
        {
            filter.put("_eq", nameOrId);
            whereCondition.put("name", filter);
        }
        variables.put("whereCondition", whereCondition);

        return graphQlClient.documentName("searchPokemonByNameOrId") 
                .variables(variables)
                .execute()
                .map(response -> response.field("pokemon_v2_pokemon").toEntityList(PokemonGraphQlResponseRecord.class));
    }

    @Cacheable(value = "getPokemonDetailsGraphQl", key = "'details-gql-' + #id")
    public Mono<PokemonGraphQlResponseRecord> getPokemonDetailsGraphQl(int id) {
        return graphQlClient.documentName("getPokemonDetails")
                .variable("id", id)
                .execute()
                .map(response -> response.field("pokemon_v2_pokemon_by_pk").toEntity(PokemonGraphQlResponseRecord.class));
    }
    

    
}
