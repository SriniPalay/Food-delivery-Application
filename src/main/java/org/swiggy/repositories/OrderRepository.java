package org.swiggy.repositories;

import org.swiggy.models.Order;

public interface OrderRepository {
    void save(Order order);
    Order findById(int orderId);

}
