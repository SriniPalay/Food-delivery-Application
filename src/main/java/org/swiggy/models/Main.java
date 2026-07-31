package org.swiggy.models;


import org.swiggy.enums.FoodCategory;
import org.swiggy.enums.FoodType;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        Address address =
                new Address(
                        "12A",
                        "MG Road",
                        "Hyderabad",
                        "Telangana",
                        "500001"
                );

        Restaurant paradise =
                new Restaurant(
                        1,
                        "Paradise",
                        address
                );

        FoodItem biryani =
                new FoodItem(
                        1,
                        "Mushroom Biryani",
                        new BigDecimal(400.0),
                        FoodType.VEG,
                        FoodCategory.MAIN_COURSE
                );


        Customer customer =
                new Customer(
                        "101",
                        "Rukmini",
                        "9876543210"
                );

        customer.addAddress(address);

        Menu menu = new Menu();
        menu.addFoodItem(biryani);
        List<FoodItem> VegItems = menu.getVegFoodItems();
        System.out.println(VegItems);

//        Order order =
//                new Order(
//                        "1001",
//                        customer,
//                        paradise,
//                        PaymentType.UPI
//                );
//
//        order.addFoodItem(biryani);

        //System.out.println(order.calculateTotal());
    }
}