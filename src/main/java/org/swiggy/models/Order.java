package org.swiggy.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final String orderId;
    private final Customer customer;
    private final Restaurant restaurant;
    private final List<FoodItem> orderItems;
    private Address address;
    private LocalDateTime orderTime;

    // Transition and dynamic parts
    private double amount;
    private PaymentType paymentType;
    private OrderStatus  orderStatus;

    public Order(String orderId,
                 Customer customer,
                 Restaurant restaurant,
                 PaymentType paymentType) {
        this.orderId = orderId;
        this.customer = customer;
        this.restaurant = restaurant;
        this.paymentType = paymentType;

        this.orderStatus = OrderStatus.PLACED;
        this.orderItems = new ArrayList<>(); // Stamps the exact time the object is created
    }

    public void addFoodItem(FoodItem item) {
        orderItems.add(item);
    }
    public double calculateTotal() {

        double total = 0;

        for (FoodItem item : orderItems) {
            total += item.getPrice();
        }

        return total;
    }

    public void cancel() {

        if (orderStatus == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Delivered order cannot be cancelled");
        }

        orderStatus = OrderStatus.CANCELLED;
    }


}
