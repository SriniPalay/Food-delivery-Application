package org.swiggy.models;

import java.math.BigDecimal;
import java.util.Objects;

public final class MenuItem {

    private final FoodItem foodItem;

    private BigDecimal price;
    private boolean available;

    public MenuItem(FoodItem foodItem,
                    BigDecimal price,
                    boolean available) {

        this.foodItem = foodItem;
        this.price = Objects.requireNonNull(price, "Price cannot be null.");
        this.available = available;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }
    public String getFoodItemName() {
        return foodItem.getName();
    }

    public void remove(String foodItem){

    }


    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void updatePrice(BigDecimal price) {
        this.price = Objects.requireNonNull(price, "Price cannot be null.");
    }

    public void markAvailable() {
        this.available = true;
    }

    public void markUnavailable() {
        this.available = false;
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