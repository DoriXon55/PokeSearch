package com.project.pokesearch.properties;


import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Arrays;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsCustomProperties {
    private List<String> allowedOrigins = List.of("http://localhost:5173");
    private List<String> allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS");
    private List<String> allowedHeaders = List.of("*");
    private boolean allowCredentials = true;
}
