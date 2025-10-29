package com.smartcart.locationprocessor.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcart.locationprocessor.model.CartRecommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartRecommendationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.cart-recommendations}")
    private String recommendationTopic;

    public void sendRecommendation(CartRecommendation recommendation) {
        try {
            String message = objectMapper.writeValueAsString(recommendation);
            
            log.info("Sending recommendation for cart {} with {} nearby items", 
                    recommendation.getCartId(), 
                    recommendation.getNearbyItemIds().size());
            
            kafkaTemplate.send(recommendationTopic, recommendation.getCartId(), message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Successfully sent recommendation for cart: {}", recommendation.getCartId());
                    } else {
                        log.error("Failed to send recommendation for cart: {}", recommendation.getCartId(), ex);
                    }
                });
                
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize recommendation for cart: {}", recommendation.getCartId(), e);
        }
    }
}
