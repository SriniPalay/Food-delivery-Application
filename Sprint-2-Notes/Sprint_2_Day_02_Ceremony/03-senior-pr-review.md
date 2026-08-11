# Sprint 2 — Day 02 Senior PR Review

## 🔴 Actual Design Problems

### 1. Assuming existing code instead of verifying it

We initially questioned Address mutability without inspecting the current Address implementation.

**Process correction:** inspect existing code whenever it affects a design decision.

### 2. Generic setStatus() would violate the Order invariant

A public status setter would allow callers to bypass the lifecycle.

### 3. Passing the original OrderItem list directly is unsafe

Avoid:

```java
this.orderItems = orderItems;
```

Use:

```java
this.orderItems = List.copyOf(orderItems);
```

### 4. Empty Order must be rejected

An Order with no items is invalid for the current business model.

---

## 🟡 Things Worth Improving

### 1. State-transition abstraction

For the current six states, keeping transition logic in Order is clear and appropriate.

If the workflow becomes much larger, consider a transition map or richer state-machine design.

Do not introduce it prematurely.

### 2. Validation depth

Validate required invariants, but do not add elaborate phone/email validation without business requirements.

### 3. Financial model

Current model:

```text
Order total = sum(OrderItem subtotals)
```

Later requirements may introduce:

```text
subtotal
discount
tax
delivery fee
platform fee
final total
```

Do not add these speculatively.

### 4. Snapshot scope

Capture only information required by Order history/business behavior. Do not blindly copy every Customer or Restaurant field.

---

## 🟢 Things We Should Leave Alone

- OrderCustomer as a separate class
- OrderRestaurant as a separate class
- Existing immutable Address
- Immutable OrderItem
- `List.copyOf()` for OrderItems
- No OrderItem setters
- No `setStatus()`
- Explicit lifecycle methods
- OrderStatus enum
- IllegalStateException for invalid transitions
- Derived total for current scope
- No speculative state-machine framework
- No unnecessary address snapshot subclasses

## PR Verdict

**Approved for current scope.**

The architecture is now:

```text
Cart
→ mutable

Checkout
→ validation + snapshot creation

Order
→ historical aggregate

Status
→ controlled lifecycle mutation
```

### Senior reviewer note

The strongest design improvement today was learning to ask:

> What information is historical, what is live, who owns it, and what is allowed to change?
