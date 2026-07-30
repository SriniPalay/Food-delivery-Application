import org.swiggy.models.*;

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
                        "1",
                        "Chicken Biryani",
                        299,
                        false
                );

        paradise.addFoodItem(biryani);

        Customer customer =
                new Customer(
                        "101",
                        "Rukmini",
                        "rukmini@gmail.com",
                        "9876543210"
                );

        customer.addAddress(address);

        Order order =
                new Order(
                        "1001",
                        customer,
                        paradise,
                        PaymentType.UPI
                );

        order.addFoodItem(biryani);

        System.out.println(order.calculateTotal());
    }
}