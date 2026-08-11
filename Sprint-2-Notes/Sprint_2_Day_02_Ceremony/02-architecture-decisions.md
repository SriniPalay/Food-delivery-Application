# Sprint 2 — Day 02 Architecture Decisions

## ADR-001 — Order uses OrderCustomer instead of live Customer

**Decision:** Order stores an OrderCustomer representation.

**Reason:** Customer is current mutable state. Order needs historical customer information.

---

## ADR-002 — Order uses OrderRestaurant instead of live Restaurant

**Decision:** Order stores an OrderRestaurant representation.

**Reason:** Restaurant data can change after an Order is placed.

---

## ADR-003 — OrderCustomer is not an Entity

**Decision:** Treat OrderCustomer as an Order-owned value/snapshot-style object.

**Reason:** It has no independent lifecycle. Its purpose is to preserve customer information for the Order.

---

## ADR-004 — OrderRestaurant is not an Entity

**Decision:** Treat OrderRestaurant as an Order-owned value/snapshot-style object.

**Reason:** The Order owns the historical representation.

---

## ADR-005 — Reuse immutable Address

**Decision:** Reuse the existing Address value object.

**Reason:** We inspected the actual implementation and confirmed it is a final class with final String fields. No additional Order-specific Address class is required.

---

## ADR-006 — Do not create snapshot classes unnecessarily

**Decision:** Do not create classes such as `OrderRestaurantAddress` unless the domain later requires different behavior.

**Reason:** Existing immutable Address already provides the required safety. This follows YAGNI.

---

## ADR-007 — Freeze OrderItems at Order creation

**Decision:** Construct Order with its complete OrderItem collection. Do not expose add/remove item operations after creation.

**Reason:** Cart is the mutable purchasing workspace. Order represents the historical purchase.

---

## ADR-008 — Order cannot be empty

**Decision:** Reject null or empty OrderItem collections.

**Reason:** A placed Order without purchased items is invalid in the current domain.

---

## ADR-009 — Defensive copy of OrderItems

**Decision:** Use `List.copyOf(orderItems)`.

**Reason:** Order must own an unmodifiable collection independent of the caller's mutable list.

---

## ADR-010 — Derive Order total for current scope

**Decision:** Calculate total from immutable OrderItem subtotals rather than storing a separate total field.

**Reason:** This avoids duplicate state and keeps a single source of truth.

**Future:** Revisit if discounts, tax, fees, refunds, invoices, or financial audit requirements make stored financial snapshots appropriate.

---

## ADR-011 — Explicit Order lifecycle

**Decision:** Use an OrderStatus enum and controlled domain methods.

**Reason:** Lifecycle transitions are business behavior and should not be freely mutated by callers.

---

## ADR-012 — No setStatus()

**Decision:** Do not expose `setStatus()`.

Use:

```text
confirm()
startPreparing()
markOutForDelivery()
markDelivered()
cancel()
```

**Reason:** Intent-based methods allow Order to enforce valid transitions.

---

## ADR-013 — Status is controlled mutable state

**Decision:** `status` remains mutable inside Order.

**Reason:** Status legitimately changes during the Order lifecycle; historical purchase data does not.

---

## ADR-014 — IllegalStateException for invalid transitions

**Decision:** Invalid lifecycle operations throw `IllegalStateException`.

**Reason:** The operation is invalid because of the current Order state, not because the supplied argument is invalid.
