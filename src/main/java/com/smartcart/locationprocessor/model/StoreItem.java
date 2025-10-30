package com.smartcart.locationprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreItem {
    
    private String id;
    
    private String itemId;
    
    private String name;
    
    private Double latitude;
    
    private Double longitude;
    
    private CosmosLocation location;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CosmosLocation {
        private String type = "Point";
        
        private double[] coordinates;
        
        public CosmosLocation(double longitude, double latitude) {
            this.type = "Point";
            this.coordinates = new double[]{longitude, latitude};
        }
    }
}
