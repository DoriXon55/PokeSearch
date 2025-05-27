package com.project.pokesearch.config;


import ch.qos.logback.core.net.server.Client;
import com.project.pokesearch.properties.GraphQlClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GraphQlConfig {

    private final GraphQlClientProperties graphQlClientProperties;


    public GraphQlConfig(GraphQlClientProperties graphQlClientProperties) {
        this.graphQlClientProperties = graphQlClientProperties;
    }
    @Bean
    public HttpGraphQlClient httpGraphQlClient() {
        final int bufferSize = graphQlClientProperties.getBufferSizeMb() * 1024 * 1024;

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(bufferSize))
                .build();

        WebClient client = WebClient.builder()
                .baseUrl(graphQlClientProperties.getBaseUrl())
                .exchangeStrategies(strategies)
                .build();
        return HttpGraphQlClient.builder(client).build();
    }
}
