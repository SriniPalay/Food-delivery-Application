# Repository Pattern — Swiggy Project Revision

## Problem
An Order needs a storage boundary. Without persistence, application memory disappears when the process stops.

## Contract
```java
public interface OrderRepository {
    void save(Order order);
    Order findById(int orderId);
}
```

The interface describes **what** the application needs, not **how** storage works.

## In-memory implementation
```java
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Integer, Order> orders;

    public InMemoryOrderRepository() {
        this.orders = new HashMap<>();
    }

    @Override
    public void save(Order order) {
        orders.put(order.getOrderId(), order);
    }

    @Override
    public Order findById(int orderId) {
        return orders.get(orderId);
    }
}
```

For learning, the `HashMap` acts as our fake database.

## Why CheckoutService should not know the implementation

Bad:
```java
this.orderRepository = new InMemoryOrderRepository();
```

Better:
```java
private final OrderRepository orderRepository;

public CheckoutService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
}
```

CheckoutService coordinates checkout; persistence implementation is a separate concern.

## Dependency Inversion
High-level checkout policy depends on `OrderRepository`, an abstraction, rather than `InMemoryOrderRepository`.

```text
CheckoutService
       |
       v
OrderRepository  <<interface>>
       ^
       |
InMemoryOrderRepository
```

Later:
```text
CheckoutService
       |
       v
OrderRepository
       ^
       |
DatabaseOrderRepository
```

CheckoutService can remain unchanged when storage changes.

## Dependency Injection
The dependency is supplied from outside:

```java
OrderRepository repository =
        new InMemoryOrderRepository();

CheckoutService checkoutService =
        new CheckoutService(repository);
```

## Multiple instances
These are different stores:

```java
OrderRepository repo1 = new InMemoryOrderRepository();
OrderRepository repo2 = new InMemoryOrderRepository();
```

Conceptually:
```text
repo1 → Map A
repo2 → Map B
```

An Order saved in A is not visible in B.

We do not solve this with a global Singleton. Instead, the composition root controls the instance that is shared. Later Spring can manage lifecycle and scope.

## OCP connection
A new implementation can be added:
```java
class DatabaseOrderRepository implements OrderRepository {
    ...
}
```
without modifying CheckoutService.

## Interview answer
> Repository isolates persistence concerns behind an abstraction. CheckoutService depends on the repository contract, while the concrete storage implementation is supplied through dependency injection.
