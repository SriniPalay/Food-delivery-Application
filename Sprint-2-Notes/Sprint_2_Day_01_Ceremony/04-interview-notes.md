# Sprint 2 — Day 01 Interview Notes

## 1. Why shouldn't OrderItem reference MenuItem?

**Strong answer:**

> MenuItem represents current catalog state and can change. OrderItem represents a historical purchase, so I capture the relevant snapshot at checkout instead of retaining a live reference. This prevents future catalog changes from modifying historical order data.

---

## 2. Why store foodItemId and foodItemName?

ID represents stable identity.

Name represents historical display information.

The name can change later, but the old Order should preserve what the customer saw/purchased.

---

## 3. Why make OrderItem fields final?

Because OrderItem represents a historical purchase.

After checkout:

```text
unitPrice
quantity
foodItemName
```

should not be arbitrarily modified.

---

## 4. Why store subtotal if it is derived?

A derived value does not automatically have to be recalculated.

We can safely store it when:

- its inputs are immutable,
- the invariant is easy to preserve,
- and the value has meaning as part of the historical financial snapshot.

Invariant:

```text
subtotal == unitPrice × quantity
```

---

## 5. Why not pass parameters to calculateSubtotal()?

Because OrderItem already owns:

```text
unitPrice
quantity
```

So:

```java
calculateSubtotal(unitPrice, quantity)
```

duplicates state already available to the object.

Prefer:

```java
calculateSubtotal()
```

inside the class.

---

## 6. Why is calculateSubtotal() private?

Callers should not care how subtotal is calculated.

They need the value:

```java
getSubtotal()
```

This is encapsulation.

---

## 7. Why BigDecimal instead of double?

Floating-point types can introduce precision problems in monetary calculations.

`BigDecimal` provides controlled decimal arithmetic appropriate for money.

---

## 8. Why not simply make every class final?

Because final is a design decision.

A class should be final when preventing subclassing protects invariants or communicates that the concept is closed.

An abstraction intended for polymorphism should remain extensible.

---

## 9. When is inheritance appropriate?

When there is a genuine behavioral IS-A relationship and the subtype can safely substitute for the parent.

Example:

```text
Payment
├── CardPayment
├── UPIPayment
└── WalletPayment
```

---

## 10. Why is `OrderItem extends MenuItem` wrong?

Because:

```text
OrderItem IS-A MenuItem
```

is not true.

They represent different concepts and lifecycles.

```text
MenuItem
→ current restaurant offering

OrderItem
→ historical purchase
```

---

## 11. What is the Trojan Horse problem?

A subtype can enter through a parent reference and unexpectedly alter behavior.

For example, if an immutable parent type allows a subclass to introduce mutation, code expecting the parent's contract may be broken.

This connects to the:

- Liskov Substitution Principle
- Behavioral contracts
- Invariants
- Encapsulation

---

## 12. Composition vs Inheritance

Use composition when the relationship is:

```text
HAS-A
```

Example:

```text
MenuItem HAS-A FoodItem
```

Consider inheritance/interface polymorphism when the relationship is:

```text
IS-A
```

and behavior is genuinely substitutable.

---

# Interview-Level Principles

### Encapsulation

Objects should control the state they own.

### Immutability

Historical data should not unexpectedly change.

### Snapshot modeling

Capture the data needed to preserve a historical business fact.

### Liskov Substitution Principle

A subtype should be safely usable anywhere its parent is expected.

### YAGNI

Do not build abstractions for hypothetical future requirements.

### Tell, Don't Ask

Put behavior close to the data that owns the behavior.

### Invariants

Objects should prevent invalid states rather than relying on every caller to behave correctly.

---

# Strong Interview Statement

> I distinguish live domain state from historical state. A MenuItem is a current offering, whereas an OrderItem is a historical purchase snapshot. Therefore I avoid holding mutable catalog references in OrderItem and capture the required values at checkout. I also protect the Order lifecycle with explicit domain operations rather than exposing a generic status setter.

