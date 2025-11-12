package com.smartcart.locationprocessor.repository;

import com.smartcart.locationprocessor.model.ItemLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemLocationRepository {

    private final MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.proximity-radius-meters:20}")
    private double proximityRadiusMeters;

    public List<ItemLocation> findItemsWithinRadius(double latitude, double longitude, double radiusMeters) {
        log.info("Querying items within {} meters of ({}, {})", radiusMeters, latitude, longitude);

        GeoJsonPoint location = new GeoJsonPoint(longitude, latitude);

        Query query = new Query(
                Criteria.where("location").nearSphere(location).maxDistance(radiusMeters)
        );

        List<ItemLocation> items = mongoTemplate.find(query, ItemLocation.class);

        log.info("Found {} items within {} meters", items.size(), radiusMeters);
        return items;
    }

    public List<ItemLocation> findNearbyItems(double latitude, double longitude) {
        return findItemsWithinRadius(latitude, longitude, proximityRadiusMeters);
    }
}