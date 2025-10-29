package com.smartcart.locationprocessor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartLocation {
    
    @NotNull
    @JsonProperty("cartId")
    private String cartId;
    
    @NotNull
    @JsonProperty("latitude")
    private Double latitude;
    
    @NotNull
    @JsonProperty("longitude")
    private Double longitude;
}
