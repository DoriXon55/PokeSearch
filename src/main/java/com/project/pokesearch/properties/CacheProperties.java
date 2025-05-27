package com.project.pokesearch.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.cache.caffeine")
public class CacheProperties {
    private long maximumSize = 2000;
    private long expireAfterWriteDays = 1;
}
