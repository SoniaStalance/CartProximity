package com.smartcart.locationprocessor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.smartcart.locationprocessor.repository")
public class MongoConfig {

    /**
     * Exposes the configured store-items collection name as a String bean.
     * This bean is then referenced by SpEL in the @Document annotation.
     */
    @Bean
    public String itemLocationCollectionName(@Value("${spring.data.mongodb.collection}") String collectionName) {
        return collectionName;
    }
}