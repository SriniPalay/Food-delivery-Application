package org.swiggy.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cart {
    private final List<CartItems> cartItems;
    private Restaurant restaurant;

    public Cart() {
        this.cartItems = new ArrayList<>();
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public List<CartItems> getCartItems() {
        return Collections.unmodifiableList(cartItems);
    }
    public void addFoodItem(MenuItem menuItem){
        Objects.requireNonNull(menuItem, "Food item cannot be null.");

        CartItems existingItem = findCartItem(menuItem.getFoodItem().getName());
        if (existingItem != null) {
            existingItem.increaseQuantity();
            return;
        }
        cartItems.add(new CartItem(menuItem));
    }
    public void removeMenuItem(int foodItemId) {

        CartItem cartItem = findCartItem(foodItemId);

        if (cartItem == null) {
            throw new IllegalArgumentException(
                    "Food Item not found in cart."
            );
        }

        if (cartItem.getQuantity() > 1) {
            cartItem.decreaseQuantity();
        } else {
            cartItems.remove(cartItem);
        }

        if (cartItems.isEmpty()) {
            restaurant = null;
        }
    }

    public void clearCart() {

        cartItems.clear();
        restaurant = null;
    }

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            total = total.add(cartItem.getSubtotal());
        }

        return total;
    }

    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    private CartItem findCartItem(int foodItemId) {

        for (CartItem cartItem : cartItems) {

            if (cartItem.getMenuItem()
                    .getFoodItem()
                    .getFoodItemId() == foodItemId) {

                return cartItem;
            }
        }

        return null;
    }


}
