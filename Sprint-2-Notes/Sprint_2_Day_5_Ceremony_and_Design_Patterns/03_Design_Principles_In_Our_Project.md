# Design Principles — Swiggy Project

## SRP — Single Responsibility
- `Cart` manages cart operations.
- `Order` manages order lifecycle.
- `CheckoutService` coordinates checkout.
- `OrderRepository` abstracts Order persistence.

We rejected putting payment gateway code directly into `Order` because payment integration is a separate concern.

## OCP — Open/Closed
We can add:
```text
InMemoryOrderRepository
DatabaseOrderRepository
```
without changing CheckoutService.

## LSP — Liskov Substitution
Any valid `OrderRepository` implementation should honor the repository contract and be usable where `OrderRepository` is expected.

## ISP — Interface Segregation
Our current repository interface is deliberately small:
```java
void save(Order order);
Order findById(int orderId);
```
We have not explored ISP deeply yet.

## DIP — Dependency Inversion
CheckoutService depends on:
```java
OrderRepository
```
not:
```java
InMemoryOrderRepository
```

## Encapsulation
We rejected:
```java
cartItem.setQuantity(-10);
```
because external code could create an invalid state.

Instead:
```java
increaseQuantity();
decreaseQuantity();
```
protect the invariant.

## Immutability
`OrderCustomer`, `OrderRestaurant`, and `OrderItem` are `final` with `final` fields and no setters. They represent historical checkout snapshots.

## Derived state
`Order.getTotal()` derives the total from immutable `OrderItem` subtotals instead of maintaining a second mutable source of truth.

## State encapsulation
Order exposes domain actions:
```java
acceptByRestaurant();
startPreparing();
markReadyForPickup();
markOutForDelivery();
markDelivered();
cancel();
```
rather than a public status setter.

## Tell, Don't Ask
Call:
```java
order.startPreparing();
```
rather than reading and manually manipulating status outside Order.

## Principle map
```text
Cart
 └── Encapsulation

Order
 ├── Encapsulation
 ├── State transition protection
 ├── Derived total
 └── Immutable order-items collection

OrderCustomer / OrderRestaurant / OrderItem
 └── Snapshot + immutability

CheckoutService
 ├── SRP
 └── Tell, Don't Ask

OrderRepository
 ├── Repository Pattern
 ├── DIP
 └── OCP
```
