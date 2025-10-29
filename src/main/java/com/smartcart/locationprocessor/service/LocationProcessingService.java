package com.smartcart.locationprocessor.service;

import com.smartcart.locationprocessor.kafka.CartRecommendationProducer;
import com.smartcart.locationprocessor.model.CartLocation;
import com.smartcart.locationprocessor.model.CartRecommendation;
import com.smartcart.locationprocessor.model.StoreItem;
import com.smartcart.locationprocessor.repository.StoreItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationProcessingService {

    private final StoreItemRepository storeItemRepository;
    private final CartRecommendationProducer recommendationProducer;

    public void processCartLocation(CartLocation cartLocation) {
        log.info("Processing location for cart: {}", cartLocation.getCartId());
        
        List<StoreItem> nearbyItems = storeItemRepository.findNearbyItems(
            cartLocation.getLatitude(),
            cartLocation.getLongitude()
        );
        
        List<String> nearbyItemIds = nearbyItems.stream()
            .map(StoreItem::getItemId)
            .collect(Collectors.toList());
        
        log.info("Found {} nearby items for cart {}", nearbyItemIds.size(), cartLocation.getCartId());
        
        CartRecommendation recommendation = new CartRecommendation(
            cartLocation.getCartId(),
            nearbyItemIds
        );
        
        recommendationProducer.sendRecommendation(recommendation);
        
        log.info("Successfully processed cart location and sent recommendation for cart: {}", 
                cartLocation.getCartId());
    }
}
