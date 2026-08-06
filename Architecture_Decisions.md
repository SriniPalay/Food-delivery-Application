# Architecture Decision Records (ADR)

---

## ADR-001

Decision

Restaurant owns Menu.

Reason

A Menu cannot exist independently.

---

## ADR-002

Decision

Menu owns FoodItems.

Reason

FoodItems belong to a specific Menu.

---

## ADR-003

Decision

Address is a Value Object.

Reason

Identity is determined by values.

---

## ADR-004

Decision

Address is immutable.

Reason

Changing an Address changes its business meaning.

---

## ADR-005

Decision

Customer owns Addresses.

Reason

Customer manages the address lifecycle.

---

## ADR-006

Decision

Address does not know Customer.

Reason

Avoid bidirectional coupling.

---

## ADR-007

Decision

Default Address is managed by Customer.

Reason

It is a business responsibility.

---

## ADR-008

Decision

House Number and Street are optional.

Reason

Not all real-world addresses contain them.

---

## ADR-009

Decision

Optional text fields are normalized to empty strings.

Reason

Avoid repetitive null handling.

---

## ADR-010

Decision

Validation methods remain private.

Reason

Validation is an implementation detail.

---

## ADR-011

Decision

Objects validate themselves.

Reason

Objects should always be born valid.

## ADR-012

Decision

Customer owns List<Address> directly.

Reason

Address management is currently simple.

Introducing AddressBook would be premature.

---

## ADR-013

Decision

Customer creates its own Address collection.

Reason

Ownership includes creation.

---

## ADR-014

Decision

Menu owns FoodItems.

Reason

Menu owns the collection.

Restaurant should not expose addFoodItem().

---

## ADR-015

Decision

Customer owns Address operations.

Reason

Customer owns the collection.

Address remains a passive Value Object.

---

## ADR-016

Decision

Address remains immutable.

Reason

Historical correctness.

Previous Orders should never change.

---

## ADR-017

Decision

Menu remains mutable.

Reason

Business naturally expects menus
to evolve over time.

---

## ADR-018

Decision

Customer equality is based on customerId.

Reason

Customer is an Entity.

Address equality remains value-based.

## ADR-019

Decision

Restaurant creates its own Menu.

Reason

Restaurant owns Menu.

Ownership includes lifecycle management.

---

## ADR-020

Decision

Main.java never creates Menu.

Reason

Only Restaurant should create
its owned objects.

---

## ADR-021

Decision

FoodItems are created independently
before being added to Menu.

Reason

Creation and association
are different business events.

---

## ADR-022

Decision

Main.java acts only as
an application orchestrator.

Reason

Business rules remain inside
domain objects.

---

## ADR-023

Decision

Collections remain private.

Only behaviour is exposed.

Examples

Customer

↓

addAddress()

Menu

↓

addFoodItem()

instead of exposing
modifiable collections.

---

## ADR-024

Decision

Customer.java Version 1.0 frozen.

Reason

Current design satisfies
Sprint 1 requirements.

Future modifications
should be driven only
by new business requirements.

# Architectural Decisions

...

# AD-018 — Cart is an Entity

## Decision

Model Cart as an Entity rather than a Value Object.

## Why

A Cart has:

- Identity
- Lifecycle
- Mutable state
- Owner (Customer)

Its contents change over time while it remains the same Cart.

---

# AD-019 — Customer Owns Cart

## Decision

Customer owns and creates Cart.

```java
private final Cart cart;
```

```java
public Customer(...) {
    this.cart = new Cart();
}
```

## Alternatives Considered

### Main.java creates Cart

Rejected.

Ownership should imply creation.

### Dependency Injection

Not required for our current domain model.

---

# AD-020 — Cart Does Not Reference Customer

## Decision

Keep the relationship unidirectional.

```text
Customer
    │
    ▼
Cart
```

## Why

Cart never requires Customer information to perform its business operations.

Avoid unnecessary coupling.

---

# AD-021 — Cart Owns CartItem

## Decision

Cart creates and manages CartItems internally.

Public API

```java
cart.addMenuItem(menuItem);
```

Internal implementation

```java
new CartItem(menuItem);
```

## Why

CartItem has no independent lifecycle.

Outside the Cart, a CartItem has no business meaning.

---

# AD-022 — One Cart Contains Items From One Restaurant

## Decision

A Cart may contain items from only one Restaurant.

## Why

Matches Swiggy's business behaviour.

Attempting to add items from another restaurant should fail.

The frontend decides whether to replace the cart.

---

# AD-023 — Backend Rejects, Frontend Confirms

## Decision

Backend throws an exception.

```java
throw new IllegalStateException(...)
```

Frontend asks:

```
Replace Cart?
```

## Why

Business validation belongs in the backend.

User interaction belongs in the frontend.

---

# AD-024 — Cart Stores Restaurant Explicitly

## Decision

```java
private Restaurant restaurant;
```

instead of deriving it from the first CartItem.

## Alternatives Considered

### cartItems.get(0)

Rejected.

Reasons:

- Assumes collection ordering
- Less expressive
- More conditional logic
- Business concept becomes hidden

---

# AD-025 — Cart Stores CartItems, Not FoodItems

## Decision

```java
List<CartItem>
```

instead of

```java
List<FoodItem>
```

## Why

Each cart entry owns additional state:

- Quantity
- Instructions
- Future customizations
- Subtotal

A FoodItem represents the product.

A CartItem represents the customer's selection.

---

# AD-026 — Read-Only Collection Exposure

## Decision

Expose

```java
Collections.unmodifiableList(cartItems)
```

instead of returning the internal list.

## Why

Prevent callers from bypassing Cart's business rules.

All modifications must go through Cart methods.

---

# AD-027 — Cart Does Not Store Total

## Decision

Calculate total dynamically.

```java
cart.getTotal()
```

## Alternatives Considered

Store

```java
private BigDecimal total;
```

Rejected.

## Why

Stored totals can become inconsistent.

Derived values remain correct.

---

# AD-028 — Introduce MenuItem Between Menu and FoodItem

## Previous Design

```
Restaurant
    ↓
Menu
    ↓
FoodItem
```

## Problem

FoodItem incorrectly appeared to belong directly to a Restaurant.

Different restaurants may sell the same FoodItem at different prices.

Calling

```java
foodItem.getRestaurant()
```

revealed the flaw.

## New Design

```
Restaurant
    ↓
Menu
    ↓
MenuItem
    ↓
FoodItem
```

## Benefits

- Restaurant-specific pricing
- Restaurant-specific availability
- Reusable FoodItem catalog
- Cleaner domain model
- Easier future expansion

Status:

Approved for refactoring in Day 10.
# AD-029 — Introduce MenuItem

## Decision

Introduce MenuItem between Menu and FoodItem.

Restaurant
↓
Menu
↓
MenuItem
↓
FoodItem

## Why

Restaurant-specific information belongs to MenuItem:

- Price
- Availability

FoodItem becomes a reusable catalog object.

---

# AD-030 — FoodItem No Longer Owns Price

## Decision

Move price from FoodItem to MenuItem.

## Why

Different restaurants may sell the same FoodItem at different prices.

Price belongs to the restaurant's offering, not the product itself.

---

# AD-031 — Menu Searches by FoodItem ID

## Decision

Replace

findFoodByName(String)

with

findMenuItemByFoodItemId(int)

## Why

Names are mutable.

IDs are stable.

Entities should be searched by identity.

---

# AD-032 — Menu Does Not Override equals()

## Decision

Menu will not override equals() or hashCode().

## Why

Menu has no independent identity.

Restaurant owns exactly one Menu.

No business requirement exists for comparing Menu objects.

---

# AD-033 — CartItem Starts with Quantity One

## Decision

CartItem constructor always creates

quantity = 1

## Why

A CartItem represents a newly selected item.

Increasing quantity is a business operation, not constructor responsibility.

---

# AD-034 — CartItem Calculates Its Own Subtotal

## Decision

Subtotal belongs to CartItem.

## Why

CartItem already owns:

- MenuItem
- Quantity

Behavior should remain close to the data it requires.

Supports Tell, Don't Ask.

---

# AD-035 — CartItem Does Not Override equals()

## Decision

CartItem will not override equals() or hashCode().

## Why

Cart never compares CartItems directly.

Duplicate detection is based on FoodItem identity.

Adding unnecessary equality methods increases maintenance without business value.