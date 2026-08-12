# Sprint 2 — Day 3 Wrap

## Theme
**Order lifecycle, state machines, cancellation rules, and the Checkout boundary**

## What we completed

### Order lifecycle

Current statuses:

```text
PLACED
→ ACCEPTED_BY_RESTAURANT
→ PREPARING
→ READY_FOR_PICKUP
→ OUT_FOR_DELIVERY
→ DELIVERED
```

Cancellation is currently allowed from:

```text
PLACED → CANCELLED
ACCEPTED_BY_RESTAURANT → CANCELLED
```

Later states are not cancellable under today's simplified business rules.

---

## State machine

We decided that `OrderStatus` owns knowledge of valid transitions through:

```java
canTransitionTo(OrderStatus nextStatus)
```

`Order` owns its current state and exposes business operations:

```java
acceptByRestaurant();
startPreparing();
markReadyForPickup();
markOutForDelivery();
markDelivered();
cancel();
```

We deliberately avoided a public `setStatus()`.

### Why?

A status setter would allow invalid transitions such as:

```text
PLACED → DELIVERED
```

The domain should control its lifecycle.

---

## Switch vs data-driven transition table

We considered:

1. A `switch` inside `OrderStatus`
2. A `Map<OrderStatus, Set<OrderStatus>>`

We chose the `switch`.

### Why?

The current state graph has only seven states and the switch is:

- explicit
- readable
- easy to debug
- easy to explain in an interview

We will only consider a data-driven representation if the transition graph actually becomes large or difficult to maintain.

This is YAGNI in practice.

---

## Testing

We successfully tested:

### Happy path

```text
PLACED
→ ACCEPTED_BY_RESTAURANT
→ PREPARING
→ READY_FOR_PICKUP
→ OUT_FOR_DELIVERY
→ DELIVERED
```

### Invalid transitions

We tested skipped/illegal transitions and confirmed they throw `IllegalStateException`.

Examples:

```text
PLACED → PREPARING ❌
ACCEPTED_BY_RESTAURANT → READY_FOR_PICKUP ❌
PLACED → DELIVERED ❌
PREPARING → CANCELLED ❌
DELIVERED → CANCELLED ❌
CANCELLED → ACCEPTED_BY_RESTAURANT ❌
```

---

# Cancellation discussion

For now:

```java
public void cancel() {
    transitionTo(OrderStatus.CANCELLED);
}
```

is enough.

We discussed what happens if cancellation later requires:

- cancellation fees
- refund calculations
- time windows
- customer vs restaurant rules
- payment state
- delivery state

At that point a `CancellationPolicy` could be justified.

We deliberately did **not** create it now.

> Create an abstraction when the current requirements give it a clear responsibility.

---

# Checkout boundary

We then designed the next major boundary:

```text
Cart
  ↓
CheckoutService
  ↓
Order
```

We chose **CheckoutService** because checkout is a use case that coordinates multiple objects.

`Cart` answers:

> What is currently in the cart?

`Order` answers:

> What was purchased and what is its current lifecycle?

`CheckoutService` answers:

> How do I turn the current shopping state into an Order?

---

# Snapshot conversions

The checkout boundary will create:

```text
Customer
    ↓
OrderCustomer

Cart.restaurant
    ↓
OrderRestaurant

CartItem
    ↓
OrderItem

Customer.defaultAddress
    ↓
Order.deliveryAddress
```

The critical conversion is:

```text
CartItem → OrderItem
```

not:

```text
MenuItem → OrderItem
```

We discovered this when `List.of(biryani, pizza)` failed because those variables were `MenuItem`s while the Order required `List<OrderItem>`.

---

# Actual APIs verified today

We inspected the real classes rather than assuming getter names.

### Customer

```java
getCustomerId()
getName()
getEmail()
getPhoneNumber()
getDefaultAddress()
getCart()
```

### Restaurant

```java
getId()
getName()
getAddress()
```

### Cart

```java
getRestaurant()
getCartItems()
isEmpty()
getTotal()
clearCart()
```

### CartItem

```java
getMenuItem()
getQuantity()
getSubtotal()
increaseQuantity()
decreaseQuantity()
```

### FoodItem

```java
getFoodItemId()
getName()
getFoodType()
getCategory()
```

---

# Checkout flow

Our current design is:

```text
checkout(customer)
      ↓
validate customer
      ↓
get customer.getCart()
      ↓
validate cart
      ↓
get customer.getDefaultAddress()
      ↓
create OrderCustomer snapshot
      ↓
create OrderRestaurant snapshot
      ↓
convert CartItems → OrderItems
      ↓
create Order
      ↓
SUCCESS ONLY
      ↓
cart.clearCart()
      ↓
return Order
```

If Order creation fails:

```text
Cart remains unchanged
```

We do not clear the cart before successful Order creation.

---

# Important architectural distinction

```text
Restaurant
→ live mutable entity

OrderRestaurant
→ historical immutable snapshot
```

```text
CartItem
→ mutable pre-checkout state

OrderItem
→ immutable purchased-item snapshot
```

This distinction is one of the most important lessons from the project.

---

# What we did NOT implement

We intentionally stopped before coding the complete `CheckoutService`.

We also did not introduce:

```text
CancellationPolicy
OrderIdGenerator
OrderRepository
PaymentService
RefundService
```

Those are future possibilities, not current requirements.

---

# Day 3 status

```text
State machine design             ✅
State machine implementation     ✅
Happy-path testing               ✅
Invalid transition testing       ✅
Cancellation design             ✅
Checkout boundary design        ✅
CheckoutService design          ✅
CheckoutService implementation  ⏳
```

## Day 3 takeaway

The biggest shift today was from:

> "How do I write these classes?"

to:

> "Which class should own this business rule?"

That is the engineering mindset we are training.
