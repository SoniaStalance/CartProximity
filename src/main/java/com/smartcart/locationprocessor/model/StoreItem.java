package com.smartcart.locationprocessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreItem {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("itemId")
    private String itemId;
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("latitude")
    private Double latitude;
    
    @JsonProperty("longitude")
    private Double longitude;
    
    @JsonProperty("location")
    private CosmosLocation location;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CosmosLocation {
        @JsonProperty("type")
        private String type = "Point";
        
        @JsonProperty("coordinates")
        private double[] coordinates;
        
        public CosmosLocation(double longitude, double latitude) {
            this.type = "Point";
            this.coordinates = new double[]{longitude, latitude};
        }
    }
}
