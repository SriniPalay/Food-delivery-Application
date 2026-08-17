package org.swiggy.services;

import org.swiggy.models.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckoutService {
    private static int nextOrderId=0;
    public Order checkout(Customer customer){
        Objects.requireNonNull(customer,"Customer cannot be null");
        Cart cart = customer.getCart();
        if (cart.isEmpty()){
            throw new IllegalStateException("Cannot checkout an empty cart");
        }
        Address deliveryAddress =
                Objects.requireNonNull(
                        customer.getDefaultAddress(),
                        "Default delivery address is required."
                );


        // 5. Create customer snapshot
        OrderCustomer orderCustomer =
                new OrderCustomer(
                        customer.getCustomerId(),
                        customer.getName(),
                        customer.getPhoneNumber(),
                        customer.getEmail()
                );
        Restaurant restaurant = cart.getRestaurant();

        OrderRestaurant orderRestaurant =
                new OrderRestaurant(
                        restaurant.getId(),
                        restaurant.getName(),
                        restaurant.getAddress()
                );
        // 7. Convert CartItems → OrderItems
        List<OrderItem> orderItems =
                new ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {

            MenuItem menuItem =
                    cartItem.getMenuItem();

            FoodItem foodItem =
                    menuItem.getFoodItem();

            OrderItem orderItem =
                    new OrderItem(
                            foodItem.getFoodItemId(),
                            foodItem.getName(),
                            menuItem.getPrice(),
                            cartItem.getQuantity()
                    );

            orderItems.add(orderItem);
        }

        Order order =
                new Order(
                        nextOrderId++,
                        orderCustomer,
                        orderRestaurant,
                        orderItems,
                        deliveryAddress,
                        LocalDateTime.now()
                );

        // 9. Clear cart ONLY after successful Order creation
        cart.clearCart();

        // 10. Return completed Order
        return order;

    }
}
