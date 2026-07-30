package org.swiggy.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {

    private final List<FoodItem> menuItems;// 1 Menu -> Many Food Items

    public Menu() {
        // Prepping the empty container!
        this.menuItems = new ArrayList<>();
    }

    public void addFoodItem(FoodItem foodItem){
        menuItems.add(foodItem);
    }
    public void removeFoodItem(FoodItem foodItem){
        menuItems.remove(foodItem);
    }
    public List<FoodItem> getMenuItems(){
        return Collections.unmodifiableList(menuItems);
    }
}
