# Sprint 2 — Day 02 Wrap

## Focus
Order design, historical snapshots, immutability, derived totals, defensive copying, and the Order status state machine.

## What we designed

```text
Order
├── orderId
├── OrderCustomer
├── OrderRestaurant
├── List<OrderItem>
├── deliveryAddress
├── orderDateTime
└── status
```

We decided that Customer and Restaurant should not be stored as live entities inside Order. Instead, Order owns historical representations:

```text
Customer   → OrderCustomer
Restaurant → OrderRestaurant
```

These are not independent Entities; they are Order-specific value/snapshot-style objects.

## OrderCustomer

```text
customerId
customerName
phone
email
```

It is a `final` class with final fields, constructor validation, getters, and no setters.

Important distinction:

> `final class` alone does not make a class immutable. The state and referenced objects must also be safely immutable and there must be no mutators.

## OrderRestaurant

```text
restaurantId
restaurantName
Address restaurantAddress
```

We inspected the existing `Address` implementation instead of assuming its mutability. The existing Address is already an immutable value object:

```java
public final class Address
```

with final String fields.

Therefore an OrderRestaurant can safely hold an Address reference without creating another address class.

## Process lesson: reduce assumptions

We explicitly agreed:

> Whenever a design decision depends on an existing class, ask for the current code and inspect it rather than reconstructing it from memory.

This prevents design drift and incorrect assumptions.

## OrderItem immutability and total

We revisited why Order total can currently be derived:

```text
Order total = sum(OrderItem.subtotal)
```

Our OrderItem has immutable:

```text
unitPrice
quantity
subtotal
```

and Order owns an unmodifiable OrderItem collection.

Therefore the total is deterministic.

We do not currently need a separate stored total field.

Important nuance:

> This does not mean derived values should never be stored.

In production financial systems, stored monetary snapshots may be appropriate for invoices, auditing, reconciliation, refunds, taxes, discounts, and fees. Our current domain is simpler.

## Why OrderItem immutability matters

If OrderItems were mutable, a separately stored total could become stale.

Our current design prevents that:

```text
OrderItem quantity  🔒
OrderItem price     🔒
OrderItem subtotal  🔒
```

Therefore:

```text
OrderItems
   ↓
subtotals remain stable
   ↓
Order total is deterministic
```

## Order item ownership

We decided not to expose:

```text
addOrderItem()
removeOrderItem()
```

after Order creation.

Cart is mutable; Order represents the historical purchase.

Therefore:

```text
Cart → mutable
Checkout → validate + snapshot
Order → purchased items frozen
```

An Order cannot be empty.

We use a defensive/unmodifiable copy:

```java
this.orderItems = List.copyOf(orderItems);
```

This protects the Order from changes to the caller's original list.

## Order constructor

The constructor establishes a valid Order:

- positive order ID
- non-null customer
- non-null restaurant
- non-null, non-empty item list
- non-null delivery address
- non-null order date/time
- initial status is `PLACED`

We intentionally do not pass total or status into the constructor.

Total is derived.

Status starts from the business-defined initial state.

## OrderStatus

We introduced:

```java
public enum OrderStatus {
    PLACED,
    CONFIRMED,
    PREPARING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}
```

Lifecycle:

```text
PLACED
 ├──→ CONFIRMED
 └──→ CANCELLED

CONFIRMED
 ├──→ PREPARING
 └──→ CANCELLED

PREPARING
 └──→ OUT_FOR_DELIVERY

OUT_FOR_DELIVERY
 └──→ DELIVERED
```

Terminal states:

```text
DELIVERED
CANCELLED
```

We rejected a generic `setStatus()`.

Instead Order exposes intent-based methods:

```text
confirm()
startPreparing()
markOutForDelivery()
markDelivered()
cancel()
```

Invalid transitions should throw `IllegalStateException`.

## Controlled mutability

The Order is not completely immutable because status legitimately changes.

```text
identity          🔒
customer          🔒
restaurant        🔒
items             🔒
address           🔒
date/time         🔒
status            🔄 controlled
```

This is a deliberate design rather than accidental mutability.

## Key lessons

- Entity vs Value Object
- Historical snapshot modeling
- Immutable domain objects
- Defensive copying
- `final` reference vs immutable object
- Single source of truth
- Derived state
- Aggregate ownership
- Domain methods instead of setters
- State machines
- Controlled mutability
- YAGNI
- `IllegalStateException`
- Verify existing code before making assumptions

## Next session

- Test valid Order transitions
- Test invalid transitions
- Complete Order getters/API
- Review the Order aggregate
- Integrate Order creation with Cart/Checkout
