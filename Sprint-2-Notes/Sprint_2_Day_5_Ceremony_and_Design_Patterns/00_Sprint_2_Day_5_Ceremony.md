# Sprint 2 — Day 5 Ceremony

## Theme
**Repository Pattern → Dependency Inversion → Dependency Injection**

### What we built
We evolved the application from:

Customer → Cart → CheckoutService → Order

to:

Customer → Cart → CheckoutService → Order → OrderRepository

with an in-memory implementation:

OrderRepository ← InMemoryOrderRepository

### Day 5 outcomes
- Created `OrderRepository` interface.
- Created `InMemoryOrderRepository`.
- Injected `OrderRepository` into `CheckoutService`.
- Saved Orders after successful construction.
- Retrieved Orders by ID.
- Verified the same repository instance is used by the application.
- Connected Repository Pattern with DIP, DI and OCP.
- Discussed why multiple `new InMemoryOrderRepository()` calls create separate stores.
- Discussed why Singleton is not our preferred solution.
- Introduced composition root / dependency lifecycle concept.
- Connected plain-Java DI to how Spring later manages beans.

### Verified output
```text
Initial status: PLACED
Order total: 400
Cart empty: true
Retrieved order: 1
```

### Key rule
CheckoutService owns/coordinates the **checkout use case**.
It does NOT own the **persistence mechanism**.

### End-of-day statement
> CheckoutService depends on the OrderRepository abstraction, not a concrete persistence implementation.
