package com.smartcart.locationprocessor.config;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosmosConfig {

    @Value("${azure.cosmos.endpoint}")
    private String endpoint;

    @Value("${azure.cosmos.key}")
    private String key;

    @Bean
    public CosmosClient cosmosClient() {
        return new CosmosClientBuilder()
                .endpoint(endpoint)
                .key(key)
                .directMode(new DirectConnectionConfig())
                .buildClient();
    }
}
