package org.swiggy.services;

import org.springframework.stereotype.Service;
import org.swiggy.models.Address;
import org.swiggy.models.Restaurant;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private final List<Restaurant> restaurants = new ArrayList<>();

    public RestaurantService() {

        Address paradiseAddress = new Address(
                "4-1-123",
                "Abids Road",
                "Abids",
                "Hyderabad",
                "Telangana",
                "500001"
        );

        Address bawarchiAddress = new Address(
                "6-3-111",
                "RTC Cross Road",
                "Musheerabad",
                "Hyderabad",
                "Telangana",
                "500020"
        );

        restaurants.add(
                new Restaurant(
                        101,
                        "Paradise",
                        paradiseAddress
                )
        );

        restaurants.add(
                new Restaurant(
                        102,
                        "Bawarchi",
                        bawarchiAddress
                )
        );
    }

    public List<Restaurant> getRestaurants() {
        return List.copyOf(restaurants);
    }

    public Restaurant getRestaurant(int restaurantId) {

        return restaurants.stream()
                .filter(restaurant -> restaurant.getId() == restaurantId)
                .findFirst()
                .orElse(null);
    }
}