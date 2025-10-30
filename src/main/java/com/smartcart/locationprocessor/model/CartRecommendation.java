package com.smartcart.locationprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRecommendation {
    
    private String cartId;
    
    private List<String> nearbyItemIds;
}
