# Sprint 2 — Day 01 Senior PR Review

## 🔴 Actual Design Problems

### 1. Duplicate unitPrice null validation

The implementation originally had both:

```java
if (unitPrice == null) { ... }
```

and:

```java
Objects.requireNonNull(unitPrice, ...);
```

Only one null check is required.

### 2. Missing negative price validation

We require:

```text
unitPrice >= 0
```

Zero is valid; negative prices are not.

### 3. Historical OrderItem must not reference mutable catalog objects

Avoid live `MenuItem` or `FoodItem` references when the Order needs historical data.

---

# 🟡 Things Worth Improving

## 1. Decide whether OrderItem should eventually be final

The class is immutable and historical, making `final` a reasonable future option.

But do not add it without considering whether the domain needs extension.

## 2. Equality semantics

We have not implemented `equals()` and `hashCode()`.

Do not add them automatically.

First decide whether OrderItem has independent identity or is meaningful only inside an Order.

## 3. Subtotal storage

Current choice is to store subtotal.

This is valid because unit price and quantity are immutable.

Revisit if the financial model becomes more complex.

## 4. Snapshot scope

Current snapshot:

```text
foodItemId
foodItemName
unitPrice
quantity
subtotal
```

Later decide whether additional information such as restaurant identity is required for historical/audit needs.

---

# 🟢 Things We Should Leave Alone

- `final` fields in OrderItem.
- No setters.
- `MenuItem` is not stored inside OrderItem.
- `FoodItem` is not stored inside OrderItem.
- Private subtotal calculation.
- Public `getSubtotal()`.
- BigDecimal for money.
- Explicit Order state transitions.
- Separate CartItem and OrderItem concepts.
- Composition between MenuItem and FoodItem.
- No speculative inheritance hierarchy.
- No unnecessary design patterns.

---

# PR Verdict

## OrderItem

**Approved after minor cleanup.**

Required cleanup:

```text
Remove duplicate unitPrice null validation.
Add negative-price validation.
```

## Design quality

The class now has:

- Strong invariants
- Immutable state
- Historical snapshot semantics
- Encapsulated calculation
- Clear public API

---

# Important Reviewer Principle

> Don't add abstraction because you can. Add it because the domain requires it.

And:

> Don't make a class final because immutability sounds good. Make it final when preventing extension protects a deliberate invariant or closed concept.
