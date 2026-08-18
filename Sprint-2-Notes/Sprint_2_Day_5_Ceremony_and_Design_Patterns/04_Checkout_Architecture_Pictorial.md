# Checkout Architecture — Pictorial Revision

## 1. Main checkout boundary

```text
                         LIVE WORLD
┌─────────────────────────────────────────────────────┐
│ Customer                                             │
│    │                                                │
│    └── Cart                                          │
│         ├── CartItem → MenuItem → FoodItem           │
│         └── CartItem → MenuItem → FoodItem           │
│                                                     │
│ Restaurant                                           │
└──────────────────────────┬──────────────────────────┘
                           │ checkout()
                           ▼
                 ┌───────────────────┐
                 │  CheckoutService   │
                 │                   │
                 │ coordinates       │
                 │ checkout use case │
                 └─────────┬─────────┘
                           │
             ┌─────────────┼─────────────┐
             ▼             ▼             ▼
      OrderCustomer  OrderRestaurant  OrderItem(s)
             │             │             │
             └─────────────┼─────────────┘
                           ▼
                       ┌───────┐
                       │ Order │
                       │PLACED │
                       └───┬───┘
                           │ save()
                           ▼
                  ┌─────────────────┐
                  │ OrderRepository │
                  └────────┬────────┘
                           ▲
                           │
                 ┌─────────┴─────────┐
                 ▼                   ▼
        InMemoryRepository    DatabaseRepository
             (today)             (later)
                           │
                           ▼
                    cart.clearCart()
```

## 2. Snapshot idea

```text
Customer   ─────────→ OrderCustomer
  LIVE                    SNAPSHOT

Restaurant ─────────→ OrderRestaurant
  LIVE                    SNAPSHOT

CartItem   ─────────→ OrderItem
  LIVE                    SNAPSHOT
```

The Order describes the checkout event, not a live view of mutable application objects.

## 3. Dependency Injection

```text
                 Main / Composition Root
                          │
                          │ chooses implementation
                          ▼
              InMemoryOrderRepository
                          │
                          │ injected
                          ▼
                   CheckoutService
                          │
                          │ depends only on
                          ▼
                   OrderRepository
```

Later:
```text
Spring Container
      │
      ├── creates repository
      ├── creates CheckoutService
      └── injects dependency
```

## 4. Multiple repository instances

```text
new InMemoryOrderRepository()
          │
          ▼
       Map A

new InMemoryOrderRepository()
          │
          ▼
       Map B

Map A != Map B
```

Therefore an Order saved in A is not automatically visible in B.

## 5. Order state machine

```text
PLACED
  │
  ▼
ACCEPTED_BY_RESTAURANT
  │
  ▼
PREPARING
  │
  ▼
READY_FOR_PICKUP
  │
  ▼
OUT_FOR_DELIVERY
  │
  ▼
DELIVERED
```

Cancellation is also governed by `OrderStatus.canTransitionTo(...)`.

## 6. Why Cart.clearCart() is safe

```text
CartItem(Biryani × 2)
       │
       │ copy required values
       ▼
OrderItem(Biryani × 2)
       │
       ▼
Order
       │
       └── List.copyOf(...)

Then:
cart.clearCart()

Cart → empty
Order → still contains OrderItem
```
