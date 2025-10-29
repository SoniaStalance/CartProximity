package com.smartcart.locationprocessor.config;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.DirectConnectionConfig;
import com.azure.cosmos.GatewayConnectionConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosmosConfig {

    @Value("${azure.cosmos.endpoint}")
    private String endpoint;

    @Value("${azure.cosmos.key}")
    private String key;

    @Value("${azure.cosmos.connection-mode:DIRECT}")
    private String connectionMode;

    @Bean
    public CosmosClient cosmosClient() {
        CosmosClientBuilder builder = new CosmosClientBuilder()
                .endpoint(endpoint)
                .key(key);
        
        if ("GATEWAY".equalsIgnoreCase(connectionMode)) {
            builder.gatewayMode(new GatewayConnectionConfig());
        } else {
            builder.directMode(new DirectConnectionConfig());
        }
        
        return builder.buildClient();
    }
}
