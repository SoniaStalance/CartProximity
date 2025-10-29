package com.smartcart.locationprocessor.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcart.locationprocessor.model.CartLocation;
import com.smartcart.locationprocessor.service.LocationProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartLocationConsumer {

    private final LocationProcessingService locationProcessingService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
        topics = "${kafka.topic.cart-locations}",
        groupId = "${kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeCartLocation(String message) {
        log.info("Received cart location message: {}", message);
        
        try {
            CartLocation cartLocation = objectMapper.readValue(message, CartLocation.class);
            log.info("Parsed cart location - CartId: {}, Lat: {}, Lon: {}", 
                    cartLocation.getCartId(), 
                    cartLocation.getLatitude(), 
                    cartLocation.getLongitude());
            
            locationProcessingService.processCartLocation(cartLocation);
            
        } catch (JsonProcessingException e) {
            log.error("Failed to parse cart location message: {}", message, e);
        } catch (Exception e) {
            log.error("Error processing cart location: {}", message, e);
        }
    }
}
