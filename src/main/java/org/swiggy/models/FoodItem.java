package org.swiggy.models;

import org.swiggy.models.enums.FoodCategory;
import org.swiggy.models.enums.FoodType;

import java.math.BigDecimal;
import java.util.Objects;

public class FoodItem {
    // Unique identifier
    private final int id;

    // Food name can be renamed
    private String name;

    // Optional description
    private String description;

    // Price changes over time
    private BigDecimal price;

    // Veg / Non Veg never changes
    private final FoodType foodType;

    // Category never changes
    private final FoodCategory category;

    // Restaurant can mark unavailable
    private boolean available;

    public FoodItem(int id,
                    String name,
                    BigDecimal price,
                    FoodType foodType,
                    FoodCategory category) {
        if (id <= 0) {
            throw new IllegalArgumentException("Food ID must be greater than zero.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Food name cannot be empty.");
        }

        Objects.requireNonNull(price, "Price cannot be null.");

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price should be greater than zero.");
        }

        Objects.requireNonNull(foodType, "Food type cannot be null.");

        Objects.requireNonNull(category, "Category cannot be null.");

        this.id = id;
        this.name = name;
        this.price = price;
        this.foodType = foodType;
        this.category = category;

        this.available = true;
    }
    public void renameFoodItem(String newName) {

        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Food name cannot be empty.");
        }

        this.name = newName;
    }

    /**
     * Update description
     */
    public void updateDescription(String description) {
        this.description = description;
    }

    /**
     * Update price
     */
    public void updatePrice(BigDecimal newPrice) {

        Objects.requireNonNull(newPrice, "Price cannot be null.");

        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price should be greater than zero.");
        }

        this.price = newPrice;
    }

    /**
     * Mark unavailable
     */
    public void markUnavailable() {
        this.available = false;
    }

    /**
     * Mark available
     */
    public void markAvailable() {
        this.available = true;
    }

    // ---------------- Getters ----------------

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }



}
