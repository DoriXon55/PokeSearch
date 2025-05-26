package com.project.pokesearch.config;


import ch.qos.logback.core.net.server.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlConfig {
    @Bean
    public HttpGraphQlClient httpGraphQlClient() {
        final int bufferSize = 16 * 1024 * 1024;
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(bufferSize))
                .build();



        WebClient client = WebClient.builder()
                .baseUrl("https://beta.pokeapi.co/graphql/v1beta")
                .exchangeStrategies(strategies)
                .build();
        return HttpGraphQlClient.builder(client).build();
    }
}
