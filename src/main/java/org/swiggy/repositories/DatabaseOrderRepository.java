package org.swiggy.repositories;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.swiggy.models.Order;

@Repository
@Primary
public class DatabaseOrderRepository implements OrderRepository {

    @Override
    public void save(Order order) {
        System.out.println("Saving order to DATABASE");
    }

    @Override
    public Order findById(int orderId) {
        System.out.println("Finding order from DATABASE");
        return null;
    }
}