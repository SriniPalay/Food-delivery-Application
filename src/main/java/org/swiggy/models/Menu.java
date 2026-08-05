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

    public void addMenuItem(MenuItem menuItem) {

        Objects.requireNonNull(menuItem, "Menu item cannot be null.");

        if (findFoodByName(menuItem.getFoodItem().getName()) != null) {
            throw new IllegalArgumentException(
                    "Food Item "
                            + menuItem.getFoodItem().getName()
                            + " already exists."
            );
        }

        menuItems.add(menuItem);
    }

    public void removeMenuItem(String foodName) {

        MenuItem menuItem = findFoodByName(foodName);

        if (menuItem == null) {
            throw new IllegalArgumentException(
                    "Food Item "
                            + foodName
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