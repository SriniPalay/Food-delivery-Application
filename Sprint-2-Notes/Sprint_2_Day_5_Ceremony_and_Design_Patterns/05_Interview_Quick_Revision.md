# Interview Quick Revision — Sprint 2 Day 5

## Repository
**Q:** Why use a repository?

**A:** To isolate persistence concerns behind an abstraction so application/domain logic does not depend directly on storage technology.

**Q:** Why not SQL inside CheckoutService?

**A:** It would mix the checkout use case with persistence responsibility and tightly couple the service to a storage implementation.

## Dependency Inversion
**Q:** What does CheckoutService depend on?

**A:** `OrderRepository`, an abstraction.

Not `InMemoryOrderRepository`.

## Dependency Injection
**Q:** What is injected?

**A:** An `OrderRepository` implementation is supplied to the CheckoutService constructor.

```java
new CheckoutService(repository);
```

## Multiple repository instances
**Q:** What happens if we do this twice?

```java
new InMemoryOrderRepository();
```

**A:** Two independent objects with two independent Maps. Data saved in one is not automatically visible in the other.

**Q:** Why not immediately make it Singleton?

**A:** A global Singleton introduces global mutable state and makes lifecycle/testing harder. Prefer lifecycle management at the composition root or DI container.

## CheckoutService
**Q:** Why does it exist?

**A:** Checkout is a business use case involving multiple domain objects. CheckoutService coordinates the conversion from the mutable shopping-cart world into a historical Order.

## Snapshot
**Q:** Why OrderCustomer instead of Customer?

**A:** Customer is a live mutable entity. OrderCustomer captures relevant checkout-time information so later customer changes do not rewrite history.

## Immutability
**Q:** Why immutable OrderItem?

**A:** It represents historical purchased data. Quantity and price should not be arbitrarily changed after checkout.

## Derived total
**Q:** Why derive total?

**A:** It can be calculated from immutable OrderItems, avoiding a duplicate mutable source of truth.

## State machine
**Q:** Why no public setStatus?

**A:** External callers could create invalid lifecycle states. Domain methods enforce valid transitions.

## Spring connection
Plain Java:
```java
OrderRepository repository =
        new InMemoryOrderRepository();

CheckoutService service =
        new CheckoutService(repository);
```

Spring later automates object creation and wiring. The architectural idea exists independently of Spring.
