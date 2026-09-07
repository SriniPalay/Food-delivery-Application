package org.swiggy.controllers;

import org.swiggy.services.RestaurantService;
import org.springframework.web.bind.annotation.*;
import org.swiggy.dto.CreateRestaurantRequest;
import org.swiggy.models.Restaurant;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }
    @GetMapping
    public List<Restaurant> getRestaurants(){
        return restaurantService.getRestaurants();
    }

    @GetMapping("/{restaurantID}")
    public Restaurant getRestaurants(@PathVariable int restaurantId){
        return restaurantService.getRestaurant(restaurantId);
    }

    @PostMapping
    public String createRestaurant(@RequestBody CreateRestaurantRequest createRestaurantRequest){
        return "Creating restaurant: "+ createRestaurantRequest.getName();
    }
}
