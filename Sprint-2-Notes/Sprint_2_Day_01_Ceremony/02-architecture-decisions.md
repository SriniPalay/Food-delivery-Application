# Sprint 2 — Day 01 Architecture Decisions

## ADR-001 — OrderItem Is a Historical Snapshot

### Context

MenuItem and FoodItem represent current catalog state and can change.

### Decision

OrderItem stores the required purchase information instead of keeping live references.

```text
foodItemId
foodItemName
unitPrice
quantity
subtotal
```

### Reason

Historical Orders must remain stable when current catalog data changes.

---

## ADR-002 — Do Not Store MenuItem in OrderItem

### Context

MenuItem is mutable/current.

### Decision

Do not use:

```java
private final MenuItem menuItem;
```

inside OrderItem.

### Reason

A live reference could cause historical Order data to change when the current menu changes.

---

## ADR-003 — Do Not Store FoodItem in OrderItem

### Context

FoodItem can also change, including its name.

### Decision

Store the relevant snapshot values instead.

### Reason

The Order should preserve what the customer purchased at that time.

---

## ADR-004 — OrderItem Is Immutable

### Context

OrderItem represents a historical purchase.

### Decision

All OrderItem state is final and no setters are exposed.

### Reason

Unit price and quantity must not change after purchase.

---

## ADR-005 — Store Subtotal

### Context

Subtotal is derived from unit price and quantity.

### Decision

Store subtotal as a final field and calculate it once during construction.

### Reason

Both inputs are immutable, so the invariant remains safe:

```text
subtotal == unitPrice × quantity
```

### Trade-off

This stores derived data, but gives a fixed historical financial value and does not create a consistency problem because all relevant state is immutable.

---

## ADR-006 — Calculation Method Uses Object State

### Context

Earlier design passed `unitPrice` and `quantity` as parameters to subtotal calculation.

### Decision

Use:

```java
private BigDecimal calculateSubtotal()
```

### Reason

OrderItem already owns the required state.

This reduces redundant parameters and keeps calculation encapsulated.

---

## ADR-007 — OrderItem Exposes `getSubtotal()`

### Decision

Expose:

```java
public BigDecimal getSubtotal()
```

and keep:

```java
calculateSubtotal()
```

private.

### Reason

Callers need the value, not the implementation of how it is calculated.

---

## ADR-008 — Order Uses Explicit State Transitions

### Context

Order status must follow business rules.

### Decision

Avoid generic:

```java
setStatus(...)
```

Use domain operations:

```text
confirm()
startPreparing()
markOutForDelivery()
markDelivered()
cancel()
```

### Reason

Order should protect its lifecycle invariants.

---

## ADR-009 — Do Not Add `final` Prematurely

### Context

OrderItem is immutable, but future extensibility has not been fully evaluated.

### Decision

Keep the class non-final for now and revisit.

### Reason

`final` should be a deliberate design boundary, not a mechanical rule.

---

## ADR-010 — Inheritance Requires Behavioral Substitution

### Decision

Do not use inheritance merely because classes share fields.

### Reason

Inheritance should represent a genuine IS-A relationship and satisfy the Liskov Substitution Principle.

Example of a possible valid abstraction:

```text
Payment
├── CardPayment
├── UPIPayment
└── WalletPayment
```

Example of a poor abstraction:

```text
OrderItem extends MenuItem
```

---

## ADR-011 — Composition for MenuItem/FoodItem

### Decision

Keep:

```text
MenuItem HAS-A FoodItem
```

rather than using inheritance.

### Reason

MenuItem represents a restaurant's offering of a FoodItem; it is not a specialized FoodItem.
