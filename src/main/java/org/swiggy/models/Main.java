package org.swiggy.models;

import org.swiggy.enums.FoodCategory;
import org.swiggy.enums.FoodType;
import org.swiggy.services.CheckoutService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        // --------------------------------------------------
        // 1. Create Address
        // --------------------------------------------------

        Address paradiseAddress =
                new Address(
                        "Hno-102",
                        "MG Road",
                        "Kukatpally",
                        "Hyderabad",
                        "Telangana",
                        "500072"
                );

        // --------------------------------------------------
        // 2. Create Restaurant
        // --------------------------------------------------

        Restaurant paradise =
                new Restaurant(
                        1,
                        "Paradise",
                        paradiseAddress
                );

        // --------------------------------------------------
        // 3. Create FoodItems
        // --------------------------------------------------

        FoodItem biryaniFood =
                new FoodItem(
                        101,
                        "Mushroom Biryani",
                        FoodType.VEG,
                        FoodCategory.MAIN_COURSE
                );

        FoodItem paneerFood =
                new FoodItem(
                        102,
                        "Paneer Tikka",
                        FoodType.VEG,
                        FoodCategory.STARTER
                );

        // --------------------------------------------------
        // 4. Create MenuItems
        //    MenuItem belongs to a Restaurant
        // --------------------------------------------------

        MenuItem biryani =
                new MenuItem(
                        paradise,
                        biryaniFood,
                        BigDecimal.valueOf(400),
                        true
                );

        MenuItem paneerTikka =
                new MenuItem(
                        paradise,
                        paneerFood,
                        BigDecimal.valueOf(250),
                        true
                );

        // --------------------------------------------------
        // 5. Add MenuItems to Restaurant's Menu
        // --------------------------------------------------

        paradise.getMenu().addMenuItem(biryani);
        paradise.getMenu().addMenuItem(paneerTikka);

        System.out.println("------ MENU ------");

        System.out.println(
                paradise.getMenu().getMenuItems()
        );

        System.out.println(
                "Available: "
                        + paradise.getMenu()
                        .getAvailableMenuItems()
        );

        System.out.println(
                "Veg Items: "
                        + paradise.getMenu()
                        .getVegMenuItems()
        );

        // --------------------------------------------------
        // 6. Create Customer
        // --------------------------------------------------

        Customer customer =
                new Customer(
                        101,
                        "Rukmini",
                        "9876543210",
                        "srinivasan@gmail.com"
                );

        customer.addAddress(paradiseAddress);

        System.out.println("\n------ CUSTOMER ------");

        System.out.println(
                "Addresses: "
                        + customer.getAllAddress()
        );

        // --------------------------------------------------
        // 7. Get Customer's Cart
        // --------------------------------------------------

        Cart cart = customer.getCart();

        // --------------------------------------------------
        // 8. Add first item
        // --------------------------------------------------

        System.out.println("\n------ ADD BIRYANI ------");

        cart.addMenuItem(biryani);

        System.out.println(
                "Restaurant: "
                        + cart.getRestaurant()
        );

        System.out.println(
                "Cart Items: "
                        + cart.getCartItems()
        );

        System.out.println(
                "Total: "
                        + cart.getTotal()
        );

        // --------------------------------------------------
        // 9. Add same item again
        // --------------------------------------------------

        System.out.println("\n------ ADD BIRYANI AGAIN ------");

        cart.addMenuItem(biryani);

        System.out.println(
                "Cart Items: "
                        + cart.getCartItems()
        );

        System.out.println(
                "Total: "
                        + cart.getTotal()
        );

        // Expected:
        // Biryani quantity = 2
        // Total = 800

        // --------------------------------------------------
        // 10. Add another item from SAME restaurant
        // --------------------------------------------------

        System.out.println("\n------ ADD PANEER TIKKA ------");

        cart.addMenuItem(paneerTikka);

        System.out.println(
                "Cart Items: "
                        + cart.getCartItems()
        );

        System.out.println(
                "Total: "
                        + cart.getTotal()
        );

        // Expected:
        // Biryani × 2 = 800
        // Paneer Tikka × 1 = 250
        // Total = 1050

        // --------------------------------------------------
        // 11. Remove one quantity of Biryani
        // --------------------------------------------------

        System.out.println("\n------ REMOVE ONE BIRYANI ------");

        cart.removeMenuItem(
                biryaniFood.getFoodItemId()
        );

        System.out.println(
                "Cart Items: "
                        + cart.getCartItems()
        );

        System.out.println(
                "Total: "
                        + cart.getTotal()
        );

        // Expected:
        // Biryani × 1
        // Paneer Tikka × 1
        // Total = 650

        // --------------------------------------------------
        // 12. Remove Biryani completely
        // --------------------------------------------------

        System.out.println("\n------ REMOVE BIRYANI ------");

        cart.removeMenuItem(
                biryaniFood.getFoodItemId()
        );

        System.out.println(
                "Cart Items: "
                        + cart.getCartItems()
        );

        // Expected:
        // Paneer Tikka × 1

        // --------------------------------------------------
        // 13. Remove final item
        // --------------------------------------------------

        System.out.println("\n------ REMOVE FINAL ITEM ------");

        cart.removeMenuItem(
                paneerFood.getFoodItemId()
        );

        System.out.println(
                "Cart Empty: "
                        + cart.isEmpty()
        );

        System.out.println(
                "Restaurant: "
                        + cart.getRestaurant()
        );

        // Expected:
        // Cart Empty: true
        // Restaurant: null

        // --------------------------------------------------
        // 14. Different Restaurant Test
        // --------------------------------------------------

        System.out.println("\n------ DIFFERENT RESTAURANT TEST ------");

        Address anotherAddress =
                new Address(
                        "Hno-20",
                        "Banjara Hills",
                        "Banjara Hills",
                        "Hyderabad",
                        "Telangana",
                        "500034"
                );

        Restaurant anotherRestaurant =
                new Restaurant(
                        2,
                        "Another Restaurant",
                        anotherAddress
                );

        FoodItem pizzaFood =
                new FoodItem(
                        103,
                        "Veg Pizza",
                        FoodType.VEG,
                        FoodCategory.MAIN_COURSE
                );

        MenuItem pizza =
                new MenuItem(
                        anotherRestaurant,
                        pizzaFood,
                        BigDecimal.valueOf(300),
                        true
                );

        anotherRestaurant.getMenu().addMenuItem(pizza);

        // Add Paradise item first
        cart.addMenuItem(biryani);

        try {

            // This should fail because pizza
            // belongs to another restaurant.
            cart.addMenuItem(pizza);

        } catch (IllegalStateException exception) {

            System.out.println(
                    "Expected exception: "
                            + exception.getMessage()
            );
        }

        // --------------------------------------------------
        // 15. Final Cart State
        // --------------------------------------------------

        System.out.println("\n------ FINAL CART ------");

        System.out.println(
                "Restaurant: "
                        + cart.getRestaurant()
        );

        System.out.println(
                "Items: "
                        + cart.getCartItems()
        );

        System.out.println(
                "Total: "
                        + cart.getTotal()
        );
        LocalDateTime localDateTime = LocalDateTime.now();;
        LocalDateTime orderDateTime = LocalDateTime.now();

        OrderCustomer orderCustomer =
                new OrderCustomer(
                        101,
                        "Rukmini",
                        "9876543210",
                        "rukmini@example.com"
                );

        OrderRestaurant orderRestaurant =
                new OrderRestaurant(
                        501,
                        "Paradise",
                        paradiseAddress
                );

        OrderItem biryaniOrderItem =
                new OrderItem(
                        biryaniFood.getFoodItemId(),
                        biryaniFood.getName(),
                        biryani.getPrice(),
                        1
                );

        OrderItem pizzaOrderItem =
                new OrderItem(
                        pizzaFood.getFoodItemId(),
                        pizzaFood.getName(),
                        pizza.getPrice(),
                        1
                );
        List<OrderItem> orderItems =
                List.of(
                        biryaniOrderItem,
                        pizzaOrderItem
                );

        CheckoutService checkoutService =
                new CheckoutService();

        Order order =
                checkoutService.checkout(customer);
        System.out.println(order.getStatus());
        System.out.println(order.getTotal());

        System.out.println(
                customer.getCart().isEmpty()
        );

//        Order order =
//                new Order(
//                        1001,
//                        orderCustomer,
//                        orderRestaurant,
//                        orderItems,
//                        anotherAddress,
//                        orderDateTime
//                );
//        order.acceptByRestaurant();
//        order.startPreparing();
//        order.cancel();


        System.out.println("Initial status: " + order.getStatus());
//
//
//        order.acceptByRestaurant();
//        order.markOutForDelivery();
//
//        order.acceptByRestaurant();
//        System.out.println("After confirm: " + order.getStatus());
//
//        order.startPreparing();
//        System.out.println("After preparing: " + order.getStatus());
//
//        order.markOutForDelivery();
//        System.out.println("After out for delivery: " + order.getStatus());
//
//        order.markDelivered();
//        System.out.println("After delivered: " + order.getStatus());
//        order.acceptByRestaurant();
//        order.markReadyForPickup();
    }

}