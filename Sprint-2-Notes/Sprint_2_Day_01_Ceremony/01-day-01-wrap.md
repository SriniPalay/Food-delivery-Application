# Sprint 2 — Day 01 Wrap

## Main Topic

**OrderItem design, immutability, historical snapshots, derived state, and inheritance/final class decisions.**

Today we continued pair programming around the Swiggy LLD and moved from `CartItem` toward the Order domain.

---

## 1. OrderItem: What Does It Represent?

We established the key distinction:

```text
CartItem
→ what the customer currently wants to buy

OrderItem
→ what the customer actually bought
```

A `CartItem` is mutable.

An `OrderItem` is historical.

Therefore an OrderItem should preserve the relevant state from the time of purchase.

---

## 2. OrderItem Fields

We settled on:

```java
private final int foodItemId;
private final String foodItemName;
private final int quantity;
private final BigDecimal unitPrice;
private final BigDecimal subtotal;
```

### Why each field?

### `foodItemId`

Provides stable identity for the purchased food.

### `foodItemName`

Preserves the historical display name.

If the current FoodItem is renamed later, the old Order should still show the name that was purchased.

### `unitPrice`

Captures the price at checkout.

The Order must not depend on the current MenuItem price.

### `quantity`

Captures how many units were purchased.

It is final because OrderItem is historical.

### `subtotal`

Represents:

```text
unitPrice × quantity
```

and is calculated once when the OrderItem is created.

---

## 3. Why OrderItem Should Not Hold MenuItem

We deliberately avoided:

```java
private final MenuItem menuItem;
```

because MenuItem represents a live/current restaurant offering.

Example:

```text
At checkout:
Biryani = ₹400

Later:
Biryani = ₹500
```

The old Order must still contain:

```text
Biryani
₹400
```

Therefore:

```text
MenuItem → current/live data

OrderItem → historical snapshot
```

This is one of the most important architectural lessons from today.

---

## 4. Why We Don't Hold FoodItem Either

FoodItem can also be mutable.

For example:

```java
foodItem.renameFoodItem(...)
```

If OrderItem held a FoodItem reference, changing the FoodItem could change what an old Order displays.

Therefore we snapshot the information needed by the OrderItem.

---

## 5. Subtotal: Store or Calculate?

We discussed two valid approaches.

### Option A — Calculate when requested

```java
public BigDecimal getSubtotal() {
    return unitPrice.multiply(
            BigDecimal.valueOf(quantity)
    );
}
```

### Option B — Store after calculating once

```java
private final BigDecimal subtotal;

this.subtotal = calculateSubtotal();
```

We chose Option B for our current model.

### Why?

Because:

```text
unitPrice → final
quantity  → final
subtotal  → final
```

Therefore the invariant:

```text
subtotal == unitPrice × quantity
```

cannot become invalid after construction.

This is an important lesson:

> Derived data does not automatically have to be recalculated. Storing it can be valid when there is a clear reason and the invariant is protected.

---

## 6. `calculateSubtotal()` Design

We changed from:

```java
calculateSubtotal(BigDecimal unitPrice, int quantity)
```

to:

```java
private BigDecimal calculateSubtotal()
```

because OrderItem already owns:

```text
unitPrice
quantity
```

The method therefore does not need redundant parameters.

The public API is:

```java
getSubtotal()
```

while the calculation remains an implementation detail.

---

## 7. Final OrderItem Design

The structure we reached:

```java
public class OrderItem {

    private final int foodItemId;
    private final String foodItemName;
    private final int quantity;
    private final BigDecimal unitPrice;
    private final BigDecimal subtotal;

    public OrderItem(...) {
        // validation
        // assign fields
        // calculate subtotal
    }

    private BigDecimal calculateSubtotal() {
        return unitPrice.multiply(
                BigDecimal.valueOf(quantity)
        );
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
```

No setters.

No MenuItem reference.

No FoodItem reference.

---

## 8. Validation

We added validation to make invalid OrderItems impossible to construct.

Rules:

```text
foodItemId > 0
foodItemName != null
foodItemName is not blank
unitPrice != null
unitPrice >= 0
quantity > 0
```

We also caught a duplicate validation mistake:

```java
if (unitPrice == null) { ... }

Objects.requireNonNull(unitPrice, ...);
```

Both were unnecessary.

Use one null check and then validate the numeric value.

---

## 9. Why `BigDecimal`?

Money should not be represented using floating-point types such as:

```java
double
float
```

We use:

```java
BigDecimal
```

because monetary calculations require predictable decimal behavior.

---

## 10. Order State Machine

We also established the Order lifecycle:

```text
PLACED
   ├──> CONFIRMED
   │       └──> PREPARING
   │              └──> OUT_FOR_DELIVERY
   │                       └──> DELIVERED
   │
   └──> CANCELLED
```

Allowed transitions:

| Current | Allowed |
|---|---|
| PLACED | CONFIRMED, CANCELLED |
| CONFIRMED | PREPARING, CANCELLED |
| PREPARING | OUT_FOR_DELIVERY |
| OUT_FOR_DELIVERY | DELIVERED |
| DELIVERED | None |
| CANCELLED | None |

We should not expose:

```java
setStatus(...)
```

Instead use domain methods such as:

```java
confirm()
startPreparing()
markOutForDelivery()
markDelivered()
cancel()
```

This lets Order protect its own business rules.

---

## 11. IllegalStateException vs IllegalArgumentException

We discussed the distinction:

```text
IllegalArgumentException
→ supplied argument is invalid

IllegalStateException
→ object is currently in an inappropriate state
```

Example:

```java
order.cancel();
```

when the order is already DELIVERED should result in an `IllegalStateException`.

---

## 12. Inheritance and `final`

We discussed whether:

```java
public class OrderItem
```

should become:

```java
public final class OrderItem
```

We decided not to make that decision prematurely.

### Why `final` can help

If OrderItem promises to be an immutable historical record, a subclass could potentially introduce mutable behavior and violate those assumptions.

Example:

```text
OrderItem
    ↑
MutableOrderItem
```

This connects to the **Trojan Horse problem** and **Liskov Substitution Principle**.

### But inheritance is not automatically bad.

Good potential example:

```text
Payment
├── CardPayment
├── UPIPayment
└── WalletPayment
```

because these can represent genuine behavioral substitutions.

Bad example:

```text
OrderItem extends MenuItem
```

because an OrderItem is not a specialized MenuItem.

---

## 13. Composition vs Inheritance

Important mental model:

```text
MenuItem HAS-A FoodItem
```

→ composition.

```text
CardPayment IS-A Payment
```

→ possible inheritance/interface polymorphism.

Shared fields alone are not enough to justify inheritance.

---

## 14. Senior Engineering Principles

Today's major principles:

- Encapsulation
- Immutability
- Snapshot modeling
- Domain invariants
- Composition over inappropriate inheritance
- Liskov Substitution Principle
- YAGNI
- Tell, Don't Ask
- Protecting historical data
- Domain-specific methods instead of generic setters

---

## 15. Next Target

Next session we continue with:

```text
Order.java
```

We need to design:

- Order identity
- Customer relationship
- Restaurant relationship
- OrderItem ownership
- Delivery address snapshot
- Order total
- State transitions
- Immutability boundaries
- Aggregate root behavior
