# Sprint 2 — Day 3 Architecture Decisions

## ADR-01 — `OrderStatus` owns transition knowledge

### Decision

`OrderStatus` owns the legal transition graph through:

```java
boolean canTransitionTo(OrderStatus nextStatus)
```

### Current graph

```text
PLACED
 ├── ACCEPTED_BY_RESTAURANT
 └── CANCELLED

ACCEPTED_BY_RESTAURANT
 ├── PREPARING
 └── CANCELLED

PREPARING
 └── READY_FOR_PICKUP

READY_FOR_PICKUP
 └── OUT_FOR_DELIVERY

OUT_FOR_DELIVERY
 └── DELIVERED

DELIVERED → terminal
CANCELLED → terminal
```

### Rationale

The lifecycle rules have a natural home in the status model.

`Order` should not become a large collection of state-condition rules.

---

## ADR-02 — `Order` exposes domain operations

Use:

```java
acceptByRestaurant();
startPreparing();
markReadyForPickup();
markOutForDelivery();
markDelivered();
cancel();
```

Avoid:

```java
setStatus(...)
```

### Rationale

Callers should express business actions, not manipulate internal state directly.

---

## ADR-03 — Keep cancellation simple for now

Current cancellation is a direct legal state transition.

No separate `CancellationPolicy` yet.

### Future trigger

Introduce a policy/domain component only if cancellation gains:

- fees
- refund logic
- time windows
- actor-specific rules
- payment dependencies
- more complex eligibility

### Principle

**YAGNI — don't build hypothetical complexity.**

---

## ADR-04 — Use `CheckoutService`

### Decision

Use:

```text
CheckoutService
```

as the application/use-case boundary.

### Responsibility

Coordinate:

```text
Customer
Cart
default delivery address
Restaurant
OrderCustomer snapshot
OrderRestaurant snapshot
OrderItem snapshots
Order creation
Cart clearing after success
```

### Why not `Cart.checkout()`?

Checkout involves more than Cart. It crosses the boundary between mutable shopping state and the historical Order model.

---

## ADR-05 — `CartItem → OrderItem`

At checkout:

```text
CartItem
  ↓
MenuItem
  ↓
FoodItem
  ↓
OrderItem
```

The snapshot stores:

```text
foodItemId
foodItemName
unitPrice
quantity
subtotal
```

It does not retain the live MenuItem/FoodItem.

---

## ADR-06 — Orders store snapshots

Use:

```text
Customer → OrderCustomer
Restaurant → OrderRestaurant
CartItem → OrderItem
```

### Rationale

The Order must preserve what was true at checkout even if live entities change later.

Examples:

```text
Restaurant renamed
Restaurant relocated
Food renamed
Menu price changed
Customer profile changed
Cart quantity changed
```

The historical Order should remain stable.

---

## ADR-07 — Reuse immutable `Address`

The current `Address` class is a `public final class` with final fields.

Therefore reusing an Address reference inside the snapshot model is safe from mutation through Address itself.

---

## ADR-08 — Clear Cart only after successful Order creation

Correct sequence:

```text
Create Order
   ↓
success
   ↓
clear Cart
```

Incorrect:

```text
clear Cart
   ↓
create Order
```

If Order creation fails, the Cart remains intact.

---

## ADR-09 — Cart owns Cart invariants

Cart already guarantees that all items belong to the same Restaurant.

CheckoutService should use:

```java
cart.getRestaurant()
```

and not duplicate Cart's restaurant-consistency rule.

### Principle

**One clear owner for each invariant.**

---

## ADR-10 — No dedicated OrderIdGenerator yet

We discussed identity generation.

For this learning project, a simple temporary mechanism is enough.

A repository/database-backed identity strategy can be introduced later when persistence becomes part of the system.

### Principle

Don't introduce infrastructure before we actually need infrastructure.
