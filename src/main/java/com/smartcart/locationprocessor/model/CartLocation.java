package com.smartcart.locationprocessor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartLocation {
    
    @NotNull
    private String cartId;
    
    @NotNull
    private Double latitude;
    
    @NotNull
    private Double longitude;
}
