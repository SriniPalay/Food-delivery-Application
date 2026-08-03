package org.swiggy.models;



import java.util.Objects;

public class Restaurant {

    // Unique identifier of the restaurant
    private final int id;

    // Restaurant name can change in future (rebranding)
    private String name;

    // Restaurant may relocate
    private Address address;

    // Every restaurant always owns one menu
    private final Menu menu;

    // Rating changes based on customer reviews
    private double rating;

    // Restaurant can be opened/closed
    private boolean open;

    /**
     * Constructor
     * Creates a restaurant in a valid state.
     */
    public Restaurant(int id, String name, Address address) {

        if (id <= 0) {
            throw new IllegalArgumentException("Restaurant ID must be greater than zero.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Restaurant name cannot be empty.");
        }

        Objects.requireNonNull(address, "Address cannot be null.");

        this.id = id;
        this.name = name;
        this.address = address;

        // Every restaurant starts with an empty menu
        this.menu = new Menu();

        // Default values
        this.rating = 0.0;
        this.open = true;
    }

    /**
     * Rename restaurant
     */
    public void renameRestaurant(String newName) {

        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Restaurant name cannot be empty.");
        }

        this.name = newName;
    }

    /**
     * Change restaurant location
     */
    public void changeAddress(Address newAddress) {

        Objects.requireNonNull(newAddress, "Address cannot be null.");

        this.address = newAddress;
    }

    /**
     * Open restaurant
     */
    public void openRestaurant() {
        this.open = true;
    }

    /**
     * Close restaurant
     */
    public void closeRestaurant() {
        this.open = false;
    }

    /**
     * Update restaurant rating
     */
    public void updateRating(double rating) {

        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating should be between 0 and 5.");
        }

        this.rating = rating;
    }

    /**
     * Add food item to menu
     */


    /**
     * Remove food item from menu
     */


    /**
     * Returns menu for read operations.
     */
    public Menu getMenu() {
        return menu;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public boolean isOpen() {
        return open;
    }

    //important
    @Override
    public String toString(){
        return this.getName() + " " + this.getAddress();
    }
}