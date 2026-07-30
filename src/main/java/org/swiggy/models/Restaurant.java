package org.swiggy.models;

public class Restaurant {
    private final String restaurantId;
    private final String name;
    private final Address address;
    private double rating;
    private boolean open;

    private final Menu menu;
    public Restaurant(String restaurantId, String name, Address address) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.address = address;

        // Default values for a brand new restaurant
        this.rating = 0.0;
        this.open = false;
        this.menu = new Menu(restaurantId + "-MENU"); // Automatically generates a linked Menu
    }

    public void addFoodItem(FoodItem item) {
        menu.addFoodItem(item);
    }

    public void removeFoodItem(FoodItem item) {
        menu.removeFoodItem(item);
    }
    public void openRestaurent(){
        open = true;
    }
    public void closeRestaurent(){
        open = false;
    }
    public void updateRating(double rating) {

        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Invalid rating");
        }

        this.rating = rating;
    }

    public Menu getMenu() {
        return menu;
    }


}
