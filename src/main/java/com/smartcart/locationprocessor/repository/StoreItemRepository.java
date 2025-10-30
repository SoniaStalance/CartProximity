package com.smartcart.locationprocessor.repository;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.SqlParameter;
import com.azure.cosmos.models.SqlQuerySpec;
import com.smartcart.locationprocessor.model.StoreItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class StoreItemRepository {

    private final CosmosContainer container;
    
    @Value("${azure.cosmos.proximity-radius-meters:10}")
    private double proximityRadiusMeters;

    public StoreItemRepository(CosmosClient cosmosClient,
                               @Value("${azure.cosmos.database}") String databaseName,
                               @Value("${azure.cosmos.container}") String containerName) {
        this.container = cosmosClient.getDatabase(databaseName).getContainer(containerName);
    }

    public List<StoreItem> findItemsWithinRadius(double latitude, double longitude, double radiusMeters) {
        log.info("Querying items within {} meters of ({}, {})", radiusMeters, latitude, longitude);
        
        String query = "SELECT * FROM c WHERE ST_DISTANCE(c.location, " +
                      "{'type': 'Point', 'coordinates':[@longitude, @latitude]}) <= @radius";
        
        SqlQuerySpec querySpec = new SqlQuerySpec(
            query,
            Arrays.asList(
                new SqlParameter("@latitude", latitude),
                new SqlParameter("@longitude", longitude),
                new SqlParameter("@radius", radiusMeters)
            )
        );
        
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();

        List<StoreItem> items = container.queryItems(querySpec, options, StoreItem.class)
                .streamByPage()
                .flatMap(page -> page.getResults().stream())
                .collect(Collectors.toList());

        log.info("Found {} items within {} meters", items.size(), radiusMeters);
        return items;
    }

    public List<StoreItem> findNearbyItems(double latitude, double longitude) {
        return findItemsWithinRadius(latitude, longitude, proximityRadiusMeters);
    }
}
