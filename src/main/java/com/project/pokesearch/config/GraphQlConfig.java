package com.project.pokesearch.config;


import ch.qos.logback.core.net.server.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlConfig {
    @Bean
    public HttpGraphQlClient httpGraphQlClient() {
        WebClient client = WebClient.builder()
                .baseUrl("https://beta.pokeapi.co/graphql/v1beta")
                .build();
        return HttpGraphQlClient.builder(client).build();
    }
}
