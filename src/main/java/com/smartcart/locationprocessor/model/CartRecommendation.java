package com.smartcart.locationprocessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRecommendation {
    
    @JsonProperty("cartId")
    private String cartId;
    
    @JsonProperty("nearbyItemIds")
    private List<String> nearbyItemIds;
}
