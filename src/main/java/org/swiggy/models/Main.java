package org.swiggy.models;

import org.swiggy.enums.FoodCategory;
import org.swiggy.enums.FoodType;

import java.math.BigDecimal;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("     SWIGGY BACKEND - DEMO PROJECT");
        System.out.println("======================================");


        /*
         * ======================================
         * Restaurant Registration
         * ======================================
         */

        System.out.println("\nCreating Restaurant...");

        Address restaurantAddress =
                new Address(
                        "12-101",
                        "MG Road",
                        "Kukatpally",
                        "Hyderabad",
                        "Telangana",
                        "500072"
                );

        Restaurant paradise =
                new Restaurant(
                        1,
                        "Paradise",
                        restaurantAddress
                );

        System.out.println("Restaurant Created Successfully.");
        System.out.println(paradise);


        /*
         * ======================================
         * Food Item Creation
         * ======================================
         */

        System.out.println("\nCreating Food Items...");

        FoodItem mushroomBiryani =
                new FoodItem(
                        1,
                        "Mushroom Biryani",
                        FoodType.VEG,
                        FoodCategory.MAIN_COURSE
                );

        MenuItem paradiseBiryani =
                new MenuItem(
                        mushroomBiryani,
                        BigDecimal.valueOf(400),
                        true
                );
        FoodItem paneerButterMasala =
                new FoodItem(
                        2,
                        "Paneer Butter Masala",
                        FoodType.VEG,
                        FoodCategory.MAIN_COURSE
                );

        FoodItem vegFriedRice =
                new FoodItem(
                        3,
                        "Veg Fried Rice",
                        FoodType.VEG,
                        FoodCategory.MAIN_COURSE
                );

        FoodItem coffee =
                new FoodItem(
                        4,
                        "Coffee",
                        FoodType.VEG,
                        FoodCategory.BEVERAGE
                );

        System.out.println("Food Items Created Successfully.");


        /*
         * ======================================
         * Menu Population
         * ======================================
         */

        System.out.println("\nAdding Food Items to Restaurant Menu...");


        System.out.println("Menu Populated Successfully.");


        /*
         * ======================================
         * Customer Registration
         * ======================================
         */

        System.out.println("\nRegistering Customer...");

        Customer customer =
                new Customer(
                        101,
                        "Rukmini",
                        "9876543210",
                        "rukmini@gmail.com"
                );

        System.out.println("Customer Registered Successfully.");


        /*
         * ======================================
         * Customer Address Management
         * ======================================
         */

        Address homeAddress =
                new Address(
                        "301",
                        "JNTU Road",
                        "KPHB",
                        "Hyderabad",
                        "Telangana",
                        "500085"
                );

        Address officeAddress =
                new Address(
                        "5th Floor",
                        "Financial District",
                        "Gachibowli",
                        "Hyderabad",
                        "Telangana",
                        "500032"
                );

        customer.addAddress(homeAddress);
        customer.addAddress(officeAddress);

        customer.changeDefaultAddress(officeAddress);

        System.out.println("\nCustomer Addresses:");

        System.out.println(customer.getAllAddress());


        /*
         * ======================================
         * Display Veg Menu
         * ======================================
         */

//        List<MenuItem> vegItems =
//                paradise.getMenu().getVegFoodItems();
//
//        System.out.println("\nVeg Menu");
//
//        vegItems.forEach(System.out::println);


        /*
         * ======================================
         * Business Rule Validation
         * ======================================
         */

        System.out.println("\nTesting Business Rules...");

        try {

            customer.addAddress(homeAddress);

        } catch (IllegalArgumentException ex) {

            System.out.println(ex.getMessage());

        }

        try {

            customer.removeAddress(officeAddress);

        } catch (IllegalStateException ex) {

            System.out.println(ex.getMessage());

        }


        /*
         * ======================================
         * Final Customer State
         * ======================================
         */

        System.out.println("\nFinal Customer Details");

        System.out.println(customer);

        System.out.println("\n======================================");
        System.out.println("      DEMO COMPLETED SUCCESSFULLY");
        System.out.println("======================================");

    }
}