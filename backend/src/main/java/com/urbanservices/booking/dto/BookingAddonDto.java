package com.urbanservices.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookingAddonDto extends BaseDto {
    
    private Long addonId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity = 1;
    
    /**
     * Calculates the total price for this addon (price * quantity)
     * @return total price as BigDecimal
     */
    public BigDecimal getTotalPrice() {
        if (price == null || quantity == null) {
            return BigDecimal.ZERO;
        }
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
