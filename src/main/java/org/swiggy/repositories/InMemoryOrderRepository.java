package org.swiggy.repositories;

import org.swiggy.models.Order;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryOrderRepository
        implements OrderRepository {

    private final Map<Integer, Order> orders;

    public InMemoryOrderRepository() {
        this.orders = new HashMap<>();
    }

    @Override
    public void save(Order order) {
        orders.put(order.getOrderId(), order);
    }

    @Override
    public Order findById(int orderId) {
        return orders.get(orderId);
    }
}