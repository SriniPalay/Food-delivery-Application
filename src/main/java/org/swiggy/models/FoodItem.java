package org.swiggy.models;

public class FoodItem {
    private final String foodItemId;
    private final String name;
    private final String description;
    private final double price;
    private final boolean isVegetarian;

    public FoodItem(String foodItemId, String name, String description, double price, boolean isVegetarian) {
        this.foodItemId = foodItemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isVegetarian = isVegetarian;
    }
    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }



}
