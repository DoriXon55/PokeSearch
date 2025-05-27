package com.project.pokesearch.config;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import com.project.pokesearch.properties.CorsCustomProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    private final CorsCustomProperties corsCustomProperties;

    public CorsConfig(CorsCustomProperties corsCustomProperties) {
        this.corsCustomProperties = corsCustomProperties;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsCustomProperties.getAllowedOrigins());
        configuration.setAllowedMethods(corsCustomProperties.getAllowedMethods());
        configuration.setAllowedHeaders(corsCustomProperties.getAllowedHeaders());
        configuration.setAllowCredentials(corsCustomProperties.isAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
