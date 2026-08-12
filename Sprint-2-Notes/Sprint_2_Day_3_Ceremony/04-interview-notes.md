# Sprint 2 — Day 3 Interview Notes

## Q1. Why shouldn't Order expose `setStatus()`?

Because Order has lifecycle rules.

A setter could allow:

```text
PLACED → DELIVERED
```

without the required intermediate states.

Instead expose domain operations:

```java
acceptByRestaurant();
startPreparing();
markReadyForPickup();
markOutForDelivery();
markDelivered();
cancel();
```

---

## Q2. Where should transition rules live?

`OrderStatus`.

It answers:

> Can I transition from the current status to the requested status?

`Order` owns the current status and invokes the transition.

---

## Q3. Why choose a switch instead of a Map?

The current state graph has only seven states.

A switch is explicit and readable.

A Map becomes attractive when the graph becomes large or needs to be data-driven.

Don't add complexity without a real need.

---

## Q4. Why shouldn't Order store Restaurant?

Restaurant is mutable.

For example:

```java
renameRestaurant()
changeAddress()
updateRating()
```

A live Restaurant reference could change the meaning of a historical Order.

Use:

```text
Restaurant → OrderRestaurant
```

---

## Q5. Why `OrderItem` instead of `MenuItem`?

`MenuItem` represents a current offering.

`OrderItem` represents the item actually purchased.

At checkout:

```text
CartItem → OrderItem
```

The OrderItem captures the relevant values at that moment.

---

## Q6. Why is CartItem mutable but OrderItem immutable?

Before checkout:

```text
CartItem.quantity
```

can change.

After checkout:

```text
OrderItem.quantity
```

must represent the actual purchase.

Therefore:

```text
CartItem → mutable
OrderItem → immutable
```

---

## Q7. Why derive Order total?

The Order should derive its total from its immutable OrderItems.

Conceptually:

```text
Order total = sum(OrderItem.subtotal)
```

This keeps the Order independent from the mutable Cart.

---

## Q8. Why use CheckoutService?

Checkout is a use case that coordinates several objects.

`Cart` manages shopping state.

`Order` manages the resulting Order and lifecycle.

`CheckoutService` coordinates the conversion:

```text
Customer
Cart
Address
Restaurant
CartItems
   ↓
Order snapshots
   ↓
Order
```

---

## Q9. What happens if Order creation fails?

The Cart should remain unchanged.

Correct:

```text
Create Order
   ↓
success
   ↓
clear Cart
```

This prevents a failed checkout from destroying the customer's cart.

---

## Q10. Would you create a CancellationPolicy?

Not yet.

If cancellation is only a simple state transition, `Order.cancel()` is enough.

If cancellation requires fees, refunds, time windows, actor-specific rules, or payment state, then a separate policy/domain service may be justified.

---

## Q11. Entity vs Snapshot

### Entity

Current mutable business object with identity.

Examples:

```text
Customer
Restaurant
FoodItem
```

### Snapshot

Historical representation captured at a point in time.

Examples:

```text
OrderCustomer
OrderRestaurant
OrderItem
```

---

## Q12. Does `final class` mean immutable?

No.

Example:

```java
public final class CartItem {
    private int quantity;
}
```

The class cannot be subclassed, but `quantity` can still change.

Therefore:

```text
final class ≠ immutable object
```

---

## Q13. What is YAGNI?

**You Aren't Gonna Need It.**

Don't implement speculative architecture.

For example, we discussed `CancellationPolicy` but did not create it because the current cancellation rules don't need it.

---

# Strong interview answer: Order lifecycle

> I would model Order as a domain object with an explicit lifecycle. The states could be PLACED, ACCEPTED_BY_RESTAURANT, PREPARING, READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED and CANCELLED. I would avoid exposing a generic status setter and instead expose domain operations such as acceptByRestaurant and markDelivered. The valid transition graph would be centralized in OrderStatus so invalid transitions are rejected. For historical correctness, the Order would store immutable snapshots of customer, restaurant and purchased items rather than live mutable entities.

---

# Strong interview answer: CheckoutService

> Checkout is an application use case because it coordinates multiple domain objects. I would use a CheckoutService to validate the customer's cart and delivery information, convert mutable CartItems into immutable OrderItems, create customer and restaurant snapshots, create the Order, and clear the Cart only after successful Order creation. The individual domain objects would continue to own their own invariants.

---

# Strong interview answer: Complex cancellation

> I would not create a cancellation service immediately. If cancellation is just a legal state transition, Order.cancel() is enough. If cancellation later involves fees, refund calculations, time windows, payment state, or actor-specific rules, I would separate those rules into a cancellation policy or domain service. The abstraction should be driven by business complexity rather than speculation.

---

# Day 3 keywords

```text
State machine
Domain behavior
Encapsulation
Invariant
Immutable snapshot
Entity
Application service
Use case
YAGNI
Single Responsibility
Historical consistency
```
