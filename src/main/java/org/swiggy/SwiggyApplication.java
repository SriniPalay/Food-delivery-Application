package org.swiggy;

// Use Spring's ApplicationContext import
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.swiggy.repositories.OrderRepository;
import org.swiggy.services.CheckoutService;

@SpringBootApplication
public class SwiggyApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(
                SwiggyApplication.class,
                args
        );

        // Removed 'new' and used the context directly
        CheckoutService checkoutService = context.getBean(CheckoutService.class);
        OrderRepository orderRepository = context.getBean(OrderRepository.class);

        System.out.println("Spring Container: " + context);
        System.out.println("Checkout Service Bean: " + checkoutService);
        System.out.println("Checkout Service Bean: " + orderRepository);
    }
}