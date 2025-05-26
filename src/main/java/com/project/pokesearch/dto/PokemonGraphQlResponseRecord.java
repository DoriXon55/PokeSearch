package com.project.pokesearch.dto;


import java.util.List;

public record PokemonGraphQlResponseRecord(
        int id,
        String name,
        List<PokemonTypeInfoRecord> pokemon_v2_pokemontypes,
        List<PokemonSpriteEntryRecord> pokemon_v2_pokemonsprites
) {
    public String getFrontDefaultSprite(){
        if (pokemon_v2_pokemonsprites != null && !pokemon_v2_pokemonsprites.isEmpty()){
            PokemonSpriteEntryRecord firstSpriteEntry = pokemon_v2_pokemonsprites.getFirst();
            if (firstSpriteEntry != null && firstSpriteEntry.sprites() != null)
            {
                return firstSpriteEntry.sprites().front_default();
            }
        }
        return null;
    }
}
