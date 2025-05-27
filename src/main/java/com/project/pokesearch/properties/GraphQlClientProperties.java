package com.project.pokesearch.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.graphql.client")
public class GraphQlClientProperties {
    private String baseUrl = "https://beta.pokeapi.co/graphql/v1beta";
    private int bufferSizeMb = 16;
}
