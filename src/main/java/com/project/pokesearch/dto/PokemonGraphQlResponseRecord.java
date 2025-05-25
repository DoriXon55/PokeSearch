package com.project.pokesearch.dto;


import java.util.List;

public record PokemonGraphQlResponseRecord(
        int id,
        String name,
        List<PokemonTypeInfoRecord> pokemon_v2_pokemontypes,
        List<PokemonSpriteDataRecord> pokemon_v2_pokemonsprites
) {
    public String getFrontDefaultSprite(){
        if (pokemon_v2_pokemonsprites != null && !pokemon_v2_pokemonsprites.isEmpty()){
            return pokemon_v2_pokemonsprites.getFirst().frontDefault();
        }
        return null;
    }
}
