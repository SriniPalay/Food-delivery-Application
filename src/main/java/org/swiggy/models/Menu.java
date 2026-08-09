package org.swiggy.models;

import org.swiggy.enums.FoodType;
import org.swiggy.models.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Menu {

    private final List<MenuItem> menuItems;

    public Menu() {
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(MenuItem menuItem){
        Objects.requireNonNull(menuItem,"Menu Item cannot be null.");
        MenuItem item = findMenuItemByFoodItemId(menuItem.getFoodItem().getFoodItemId());
        if (item!=null){
            throw new IllegalArgumentException("Food Item already exists in the menu.");
        }
        menuItems.add(menuItem);
    }

    private MenuItem findMenuItemByFoodItemId (int itemId){
        for (MenuItem menuItem: menuItems){
            if (menuItem.getFoodItem().getFoodItemId()==itemId){
                return menuItem;
            }
        }
        return null;
    }

    public void removeMenuItem(int foodItemId) {

        MenuItem menuItem = findMenuItemByFoodItemId(foodItemId);

        if (menuItem == null) {
            throw new IllegalArgumentException(
                    "Food Item "
                            + foodItemId
                            + " not found."
            );
        }

        menuItems.remove(menuItem);
    }

    public MenuItem findFoodByName(String foodName) {

        Objects.requireNonNull(foodName, "Food name cannot be null.");

        for (MenuItem menuItem : menuItems) {

            if (menuItem.getFoodItem()
                    .getName()
                    .equalsIgnoreCase(foodName)) {

                return menuItem;
            }
        }

        return null;
    }

    public List<MenuItem> getAvailableMenuItems() {

        List<MenuItem> availableMenuItems = new ArrayList<>();

        for (MenuItem menuItem : menuItems) {

            if (menuItem.isAvailable()) {
                availableMenuItems.add(menuItem);
            }
        }

        return Collections.unmodifiableList(availableMenuItems);
    }

    public List<MenuItem> getVegMenuItems() {

        List<MenuItem> vegMenuItems = new ArrayList<>();

        for (MenuItem menuItem : menuItems) {

            if (menuItem.getFoodItem().getFoodType() == FoodType.VEG) {
                vegMenuItems.add(menuItem);
            }
        }

        return Collections.unmodifiableList(vegMenuItems);
    }
    public List<MenuItem> getMenuItems() {
        return Collections.unmodifiableList(menuItems);
    }
}