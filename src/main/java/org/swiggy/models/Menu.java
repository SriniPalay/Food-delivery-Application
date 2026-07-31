package org.swiggy.models;

import org.swiggy.enums.FoodType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Menu {

    /**
     * Menu owns its food items.
     * The reference never changes,
     * but the contents do.
     */
    private final List<FoodItem> foodItems;

    /**
     * Constructor
     */
    public Menu() {
        this.foodItems = new ArrayList<>();
    }

    /**
     * Add a new food item to the menu.
     */
    public void addFoodItem(FoodItem foodItem) {

        Objects.requireNonNull(foodItem, "Food item cannot be null.");

        if (findFoodById(foodItem.getId()) != null) {
            throw new IllegalArgumentException(
                    "Food Item with ID "
                            + foodItem.getId()
                            + " already exists."
            );
        }

        foodItems.add(foodItem);
    }

    /**
     * Remove food item by ID.
     */
    public void removeFoodItem(int foodItemId) {

        FoodItem foodItem = findFoodById(foodItemId);

        if (foodItem == null) {
            throw new IllegalArgumentException(
                    "Food Item with ID "
                            + foodItemId
                            + " not found."
            );
        }

        foodItems.remove(foodItem);
    }

    /**
     * Search food by ID.
     */
    public FoodItem findFoodById(int foodItemId) {

        for (FoodItem foodItem : foodItems) {

            if (foodItem.getId() == foodItemId) {
                return foodItem;
            }

        }

        return null;
    }

    /**
     * Search food by Name.
     */
    public FoodItem findFoodByName(String foodName) {

        Objects.requireNonNull(foodName, "Food name cannot be null.");

        for (FoodItem foodItem : foodItems) {

            if (foodItem.getName().equalsIgnoreCase(foodName)) {
                return foodItem;
            }

        }

        return null;
    }

    /**
     * Returns all available food items.
     */
    public List<FoodItem> getAvailableFoodItems() {

        List<FoodItem> availableFoodItems = new ArrayList<>();

        for (FoodItem foodItem : foodItems) {

            if (foodItem.isAvailable()) {
                availableFoodItems.add(foodItem);
            }

        }

        return Collections.unmodifiableList(availableFoodItems);
    }

    /**
     * Returns all vegetarian food items.
     */
    public List<FoodItem> getVegFoodItems() {

        List<FoodItem> vegFoodItems = new ArrayList<>();

        for (FoodItem foodItem : foodItems) {

            if (foodItem.getFoodType() == FoodType.VEG) {
                vegFoodItems.add(foodItem);
            }

        }

        return Collections.unmodifiableList(vegFoodItems);
    }

    /**
     * Returns the complete menu as a read-only list.
     */
    public List<FoodItem> getFoodItems() {
        return Collections.unmodifiableList(foodItems);
    }

}