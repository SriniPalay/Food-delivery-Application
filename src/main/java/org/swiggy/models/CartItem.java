package org.swiggy.models;

import java.math.BigDecimal;
import java.util.Objects;

public final class CartItem {
    private final MenuItem menuItem;
    private int quantity;
    public CartItem(MenuItem menuItem) {

        this.menuItem =
                Objects.requireNonNull(
                        menuItem,
                        "Menu Item cannot be null."
                );
        this.quantity = 1;
    }
    public MenuItem getMenuItem(){
        return menuItem;
    }
    public int getQuantity(){
        return quantity;
    }
    public void increaseQuantity(){
        quantity+=1;
    }
    public void decreaseQuantity(){
        if (quantity <= 1) {
            throw new IllegalStateException(
                    "Quantity cannot be less than one."
            );
        }
        quantity-=1;
    }
    public BigDecimal getSubTotal(){
        BigDecimal subTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
        return subTotal;
    }
    @Override
    public String toString() {
        return "CartItem{" +
                "menuItem=" + menuItem +
                ", quantity=" + quantity +
                '}';
    }

}
