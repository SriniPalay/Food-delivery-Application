package org.swiggy.models;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderItem {
    private final int foodItemId;
    private final String foodItemName;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal subtotal;
    public OrderItem(int foodItemId,
                     String foodItemName,
                     BigDecimal unitPrice,
                     int quantity){
        if (foodItemId <=0){
            throw new IllegalArgumentException("Food Item ID must be greater than zero");
        }
        if (foodItemName == null || foodItemName.isBlank()) {
            throw new IllegalArgumentException(
                    "Food item name cannot be empty."
            );
        }

        Objects.requireNonNull(unitPrice,"Unit price cannot be null");

        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Unit price cannot be negative."
            );
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }
        this.foodItemId = foodItemId;
        this.foodItemName = foodItemName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = calculateSubtotal();
    }
    private BigDecimal calculateSubtotal(){
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return subtotal;
    }
    public int getFoodItemId(){
        return foodItemId;
    }
    public String getFoodItemName(){
        return foodItemName;
    }
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }
    public BigDecimal getSubtotal(){
        return subtotal;
    }
}
