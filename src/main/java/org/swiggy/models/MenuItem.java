package org.swiggy.models;

import java.math.BigDecimal;
import java.util.Objects;

public final class MenuItem {

    private final FoodItem foodItem;
    private BigDecimal price;
    private boolean available;
    public MenuItem(FoodItem foodItem,
                    BigDecimal price,
                    boolean available){
        this.foodItem = Objects.requireNonNull(foodItem, "Food Item cannot be null.");
        updatePrice(price);
        this.available = available;
    }
    public void updatePrice(BigDecimal price){
        Objects.requireNonNull(price,"Price cannot be null");
        if (price.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Price must be greater than Zero");
        }
        this.price = price;
    }
    public void markAvailable(){
        this.available = true;
    }
    public void markUnavailable(){
        this.available = false;
    }
    public boolean isAvailable(){
        return available;
    }
    public FoodItem getFoodItem(){
        return foodItem;
    }
    public BigDecimal getPrice(){
        return price;
    }
    @Override
    public String toString() {
        return "MenuItem{" +
                "foodItem=" + foodItem +
                ", price=" + price +
                ", available=" + available +
                '}';
    }
}