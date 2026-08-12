package org.swiggy.models;

import java.util.Objects;

public final class OrderRestaurant {
    private final int restaurantId;
    private final String restaurantName;
    private final Address restaurantAddress;

    public OrderRestaurant(
            int restaurantId,
            String restaurantName,
            Address restaurantAddress) {

        if (restaurantId <= 0) {
            throw new IllegalArgumentException(
                    "Restaurant ID must be greater than zero."
            );
        }

        if (restaurantName == null || restaurantName.isBlank()) {
            throw new IllegalArgumentException(
                    "Restaurant name cannot be empty."
            );
        }

        Objects.requireNonNull(
                restaurantAddress,
                "Restaurant address cannot be null."
        );

        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Address getRestaurantAddress() {
        return restaurantAddress;
    }
}
