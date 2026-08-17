package org.swiggy.models;

import org.swiggy.enums.OrderStatus;
//import org.swiggy.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final int orderId;

    private final OrderCustomer orderCustomer;
    private final OrderRestaurant orderRestaurant;
    private final List<OrderItem> orderItems;
    private final Address deliveryAddress;
    private final LocalDateTime orderDateTime;
    private OrderStatus status;

    public Order(int orderId,
                 OrderCustomer orderCustomer,
                 OrderRestaurant orderRestaurant,
                 List<OrderItem> orderItems,
                 Address deliveryAddress,
                 LocalDateTime orderDateTime) {
        if (orderId<=0){
            throw new IllegalArgumentException("Order ID should be greater than 0");
        }
        Objects.requireNonNull(orderCustomer,"Customer details cannot be empty");
        Objects.requireNonNull(orderRestaurant,"Restaurant details cannot be empty");
        Objects.requireNonNull(orderItems,"Order item details cannot be empty");
        if (orderItems.isEmpty()){
            throw new IllegalArgumentException("Order Items list cannot be empty");
        }
        Objects.requireNonNull(
                deliveryAddress,
                "Delivery address cannot be null."
        );

        Objects.requireNonNull(
                orderDateTime,
                "Order date and time cannot be null."
        );

        this.orderId = orderId;
        this.orderCustomer = orderCustomer;
        this.orderRestaurant = orderRestaurant;

        // Defensive copy
        this.orderItems = List.copyOf(orderItems);

        this.deliveryAddress = deliveryAddress;
        this.orderDateTime = orderDateTime;

        // Every newly created order starts in PLACED state.
        this.status = OrderStatus.PLACED;
    }
    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItem orderItem : orderItems) {
            total = total.add(orderItem.getSubtotal());
        }

        return total;
    }
    private void transitionTo(OrderStatus nextStatus) {

        if (!status.canTransitionTo(nextStatus)) {
            throw new IllegalStateException(
                    "Invalid order status transition from "
                            + status
                            + " to "
                            + nextStatus
            );
        }

        status = nextStatus;
    }
    public void acceptByRestaurant() {
        transitionTo(OrderStatus.ACCEPTED_BY_RESTAURANT);
    }

    public void startPreparing() {
        transitionTo(OrderStatus.PREPARING);
    }

    public void markReadyForPickup() {
        transitionTo(OrderStatus.READY_FOR_PICKUP);
    }

    public void markOutForDelivery() {
        transitionTo(OrderStatus.OUT_FOR_DELIVERY);
    }

    public void markDelivered() {
        transitionTo(OrderStatus.DELIVERED);
    }

    public void cancel() {
        transitionTo(OrderStatus.CANCELLED);
    }
    public OrderStatus getStatus() {
        return status;
    }

}



