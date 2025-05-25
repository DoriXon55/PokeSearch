package com.project.pokesearch.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record PokemonSpriteDataRecord(String sprites) {
    public String frontDefault() {
        if (sprites == null || sprites.isBlank()) return null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(sprites);
            if (rootNode.has("front_default") && !rootNode.get("front_default").isNull()) {
                return rootNode.get("front_default").asText();
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
