package org.swiggy.models;

import org.swiggy.enums.FoodCategory;
import org.swiggy.enums.FoodType;
import java.util.Objects;

public class FoodItem {
    // Unique identifier
    private final int foodItemId;
    // Food name can be renamed
    private String name;
    // Veg / Non Veg never changes
    private final FoodType foodType;
    // Category never changes
    private final FoodCategory category;

    public FoodItem(int foodItemid,
                    String name,
                    FoodType foodType,
                    FoodCategory category) {
        this.foodItemId = validateFoodItemId(foodItemid);
        this.name = normalizeName(name);
        this.foodType =
                Objects.requireNonNull(foodType, "Food type cannot be null.");

        this.category =
                Objects.requireNonNull(category, "Food category cannot be null.");
    }
    public void renameFoodItem(String newName){
        this.name = normalizeName(newName);
    }
    private int validateFoodItemId(int foodItemid){
        if (foodItemid<=0){
            throw new IllegalArgumentException("Food item ID cannot be zero or less than zero");
        }
        return foodItemid;
    }
    private String normalizeName(String name){
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Food name cannot be empty.");
        }
        return name.trim();
    }
    public int getFoodItemId() {
        return foodItemId;
    }

    public String getName() {
        return name;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public FoodCategory getCategory() {
        return category;
    }
    // ---------------- Equality ----------------

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FoodItem other)) {
            return false;
        }

        return foodItemId == other.foodItemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodItemId);
    }

    @Override
    public String toString() {
        return "FoodItem{" +
                "foodItemId=" + foodItemId +
                ", name='" + name + '\'' +
                ", foodType=" + foodType +
                ", category=" + category +
                '}';
    }




}
