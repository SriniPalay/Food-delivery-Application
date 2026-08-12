# Sprint 2 — Day 3 Senior PR Review

## Overall verdict

### APPROACHING MERGE ✅

The Order lifecycle and snapshot design are now solid for the current scope.

---

# 🔴 Actual design problems

## 1. Confusing `MenuItem` and `OrderItem`

`MenuItem` represents a current restaurant offering.

`OrderItem` represents the purchased item.

The conversion must happen at checkout:

```text
CartItem → OrderItem
```

---

## 2. Allowing arbitrary status mutation

Avoid:

```java
order.setStatus(...)
```

The state machine should control legal transitions.

Use domain operations instead.

---

## 3. Storing live mutable entities inside Order

Do not make Order depend directly on mutable:

```text
Customer
Restaurant
FoodItem
MenuItem
```

Use immutable historical snapshots.

---

# 🟡 Things worth improving later

## CheckoutService

Designed today, implementation remains for the next session.

Target:

```java
Order checkout(Customer customer)
```

## Order ID generation

Use a temporary approach now; introduce repository/database identity later.

## Cart naming

Potential future improvement:

```java
clearCart()
```

could read more naturally as:

```java
clear()
```

Not a blocker.

## Cancellation

If requirements become more complex, introduce a cancellation policy.

## Transition representation

The current switch is good.

A data-driven transition table may be considered only when the state graph becomes sufficiently complex.

---

# 🟢 Things to leave alone

### CartItem

Final class but intentionally mutable.

```text
CartItem
→ current shopping state
```

### OrderItem

Immutable historical state.

```text
OrderItem
→ purchased state
```

### OrderStatus

Enum is appropriate for the current closed set of states.

### `OrderStatus.canTransitionTo()`

Good responsibility placement.

### Unmodifiable Cart list

Keep:

```java
Collections.unmodifiableList(cartItems)
```

It prevents callers from directly modifying Cart's internal collection.

### Mutable Restaurant

Correct because Restaurant is a live entity.

### Immutable OrderRestaurant

Correct because it is a historical snapshot.

---

# Senior reviewer principle

A PR review is not:

> "What can I refactor?"

It is:

> "What is actually wrong, what is risky, and what is already good?"

Our classification:

```text
🔴 Must fix
    model-boundary mistakes
    uncontrolled state mutation

🟡 Improve when justified
    checkout orchestration
    ID generation
    cancellation complexity
    transition representation

🟢 Leave alone
    existing encapsulation
    snapshot design
    intentional mutability
```

---

# Key design principles demonstrated

- Encapsulation
- Immutability
- Entity vs snapshot
- State machines
- Domain behavior
- Invariants
- Single Responsibility
- Application/use-case services
- YAGNI
- Historical consistency
