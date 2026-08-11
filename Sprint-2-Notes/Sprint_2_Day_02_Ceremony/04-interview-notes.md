# Sprint 2 — Day 02 Interview Notes

## 1. Why doesn't Order hold the live Customer?

Customer represents current mutable state, while Order represents a historical transaction. I capture the relevant customer information in an Order-specific immutable representation so future profile changes do not alter historical Orders.

---

## 2. Why use OrderCustomer instead of extending Customer?

Because they represent different concepts:

```text
Customer
→ current Entity

OrderCustomer
→ historical Order-specific representation
```

This is composition/value modeling, not inheritance.

---

## 3. Does final class mean immutable?

No.

`final class` prevents subclassing.

Immutability also requires:

- immutable state
- no mutators
- safe referenced objects
- valid construction

---

## 4. Why can Address be shared?

Because the existing Address implementation was inspected and confirmed to be immutable. It is a final class with final String fields.

Therefore defensive copying is unnecessary.

---

## 5. Why use List.copyOf()?

A final list reference is not enough because the list contents can still be changed.

`List.copyOf()` creates an unmodifiable copy and prevents the caller's original mutable list from becoming an indirect mutation path.

---

## 6. Why doesn't Order have addOrderItem()?

Cart is the mutable collection used before checkout. Once checkout creates an Order, purchased items represent historical facts.

Therefore OrderItems are frozen after creation.

---

## 7. Why is Order total derived?

In the current model:

```text
Order total = sum(OrderItem subtotals)
```

OrderItems are immutable, so the result is deterministic.

Deriving the total avoids duplicate state and maintains a single source of truth.

---

## 8. Could total be stored?

Yes.

Production financial systems may intentionally persist monetary snapshots for invoices, auditing, reconciliation, refunds, taxes, discounts, and fees.

The decision depends on domain and persistence requirements.

---

## 9. Why not double for money?

Use BigDecimal when precise decimal monetary arithmetic is required. Floating-point types such as double can introduce representation/rounding problems.

---

## 10. Why not setStatus()?

A generic setter lets callers bypass business rules.

Instead:

```text
confirm()
startPreparing()
markOutForDelivery()
markDelivered()
cancel()
```

express business intent and allow Order to validate transitions.

---

## 11. Why IllegalStateException?

The operation is invalid because of the object's current state.

Example:

```text
DELIVERED → cancel()
```

The cancellation request isn't an invalid argument; the Order is in an inappropriate state for cancellation.

---

## 12. What is controlled mutability?

Not every domain object needs to be completely immutable.

For Order:

```text
identity          immutable
customer          immutable
restaurant        immutable
items             immutable
address           immutable
date/time         immutable
status            controlled mutable
```

Status changes are allowed only through domain operations.

---

## 13. Cart vs Order

### Cart

- mutable
- can add/remove items
- can be empty
- represents current intent

### Order

- historical
- cannot be empty
- purchased items are frozen
- represents the completed purchase
- status changes through controlled lifecycle operations

---

## 14. Aggregate Root

In our current model:

```text
Order
├── OrderCustomer
├── OrderRestaurant
├── OrderItem
├── OrderItem
└── Address
```

Order is the object responsible for protecting the aggregate's business rules and lifecycle.

External code should primarily interact through Order behavior.

---

# Strong interview answer

> I model Order as a historical aggregate. During checkout, mutable Cart data is transformed into immutable Order-specific representations such as OrderCustomer, OrderRestaurant, and OrderItem. The Order owns its OrderItem collection through a defensive copy. Its status remains controlled mutable state and can only transition through explicit domain methods. This prevents callers from changing historical purchase information or creating invalid lifecycle states.

---

# Key concepts to revise

- Entity vs Value Object
- Historical snapshot modeling
- Immutability
- Defensive copying
- `final` reference vs immutable object
- Aggregate Root
- State machine
- Domain behavior
- Encapsulation
- Single source of truth
- Derived state
- YAGNI
- IllegalStateException
