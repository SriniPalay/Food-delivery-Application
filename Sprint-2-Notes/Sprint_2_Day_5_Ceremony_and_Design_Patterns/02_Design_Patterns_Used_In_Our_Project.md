# Design Patterns — Only Our Swiggy Project

This is a project-specific revision sheet. It does not claim patterns we have not implemented.

## 1. Repository Pattern

**Where:** `OrderRepository` + `InMemoryOrderRepository`

```java
public interface OrderRepository {
    void save(Order order);
    Order findById(int orderId);
}
```

**Problem solved:** separates persistence from application/domain logic.

---

## 2. Dependency Injection

**Where:** `CheckoutService`

```java
public CheckoutService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
}
```

Composition:
```java
OrderRepository repository =
        new InMemoryOrderRepository();

CheckoutService service =
        new CheckoutService(repository);
```

**Problem solved:** CheckoutService receives its dependency instead of constructing a concrete implementation.

---

## 3. State Machine / State Transition Modeling

**Where:** `Order` + `OrderStatus`

```java
PLACED,
ACCEPTED_BY_RESTAURANT,
PREPARING,
READY_FOR_PICKUP,
OUT_FOR_DELIVERY,
DELIVERED,
CANCELLED
```

Transitions are centralized:

```java
private void transitionTo(OrderStatus nextStatus) {
    if (!status.canTransitionTo(nextStatus)) {
        throw new IllegalStateException(...);
    }
    status = nextStatus;
}
```

Domain operations:
```java
acceptByRestaurant();
startPreparing();
markReadyForPickup();
markOutForDelivery();
markDelivered();
cancel();
```

**Problem solved:** invalid order lifecycle transitions are rejected.

---

## 4. Snapshot / Immutable historical representation

This is an architectural technique rather than a GoF pattern.

```text
Customer   → OrderCustomer
Restaurant → OrderRestaurant
CartItem   → OrderItem
```

Example:
```java
OrderCustomer orderCustomer =
        new OrderCustomer(
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail()
        );
```

**Problem solved:** an Order records checkout-time information rather than remaining coupled to live mutable objects.

---

## 5. Defensive Copy / Immutable Collection

`Order` uses:
```java
this.orderItems = List.copyOf(orderItems);
```

`OrderItem` is immutable:
```java
private final int foodItemId;
private final String foodItemName;
private final int quantity;
private final BigDecimal unitPrice;
private final BigDecimal subtotal;
```

**Problem solved:** historical Order data cannot be accidentally mutated through the source collection.

---

## 6. Tell, Don't Ask

Examples:
```java
order.startPreparing();
```

instead of allowing arbitrary:
```java
order.setStatus(...);
```

and:
```java
checkoutService.checkout(customer);
```

instead of putting the complete checkout algorithm into `Main`.

---

## Patterns NOT yet implemented
Do not claim these are already part of our project:
- Factory
- Strategy
- Observer
- Builder
- Adapter
- Decorator
- Command

We will add patterns only when a real project problem justifies them.
