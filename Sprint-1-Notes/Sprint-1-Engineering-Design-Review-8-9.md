# Sprint 1 — Day 9

## Theme

Good software begins with a good domain model.

Today was less about writing code and more about discovering flaws in the domain model before implementing additional functionality.

---

# Goals

- Finalize Cart design
- Decide ownership
- Start Cart implementation
- Review business rules
- Improve Menu domain model

---

# Cart Ownership

Customer owns Cart.

Customer is responsible for creating Cart.

```java
public Customer(...) {

    this.cart = new Cart();

}
```

Reason:

The owner should create its owned objects.

---

# Cart Does Not Know Customer

Rejected design:

```java
private Customer customer;
```

Reason:

Cart never needs Customer to perform its responsibilities.

Prefer one-way relationships.

Customer
↓
Cart

---

# Cart Owns CartItem

CartItem has no independent lifecycle.

Main.java should never create CartItem.

Instead:

```java
cart.addMenuItem(menuItem);
```

Cart internally creates CartItem.

---

# Cart Fields

```java
private Restaurant restaurant;

private final List<CartItem> cartItems;
```

Restaurant is mutable.

Reason:

The cart may later be cleared and reused for another restaurant.

cartItems is final.

The collection never changes.

Only its contents change.

---

# Cart Constructor

```java
public Cart() {

    this.cartItems = new ArrayList<>();

}
```

Restaurant remains null until the first item is added.

---

# Read-only Collections

```java
Collections.unmodifiableList(cartItems)
```

Purpose:

Prevent external code from:

- add()
- remove()
- clear()

All modifications should happen through Cart methods.

---

# Backend vs Frontend

Business Rule:

A cart cannot contain items from multiple restaurants.

Backend:

```java
throw new IllegalStateException(...);
```

Frontend:

Ask the user:

Replace Cart?

The backend validates.

The frontend communicates.

---

# Major Domain Discovery

Original model:

Restaurant
↓
Menu
↓
FoodItem

Problem:

FoodItem does not naturally belong to a Restaurant.

Calling:

foodItem.getRestaurant()

revealed a flaw in the domain model.

---

# New Domain Model

Restaurant
↓
Menu
↓
MenuItem
↓
FoodItem

MenuItem becomes the restaurant-specific representation of a FoodItem.

FoodItem becomes a reusable catalog object.

---

# MenuItem (Temporary Design)

Fields:

- FoodItem
- Price
- Availability

Tomorrow these will be reviewed and redesigned.

---

# Cart Methods Designed

Constructor

getRestaurant()

getCartItems()

addMenuItem()

removeMenuItem()

clearCart()

getTotal()

findCartItem()

Most methods are implemented in Version 1 and will undergo PR review after MenuItem redesign.

---

# Open Questions For Tomorrow

Should MenuItem know Restaurant?

Should MenuItem know Menu?

Should CartItem implement equals/hashCode()?

Should quantity validation prevent zero?

Should subtotal belong to CartItem or Cart?

Should searches use FoodItem ID instead of name?

---

# Today's Biggest Learning

The most valuable outcome today was not writing Cart.

It was recognizing that the domain model had become inconsistent.

Instead of building more code on top of it,

we stopped,

questioned the model,

and redesigned it.

That is a habit of good software engineers.

---

# Progress

Completed:

✔ Cart ownership

✔ Cart fields

✔ Cart constructor

✔ Collection encapsulation

✔ Backend responsibility

✔ Cart Version 1

✔ Domain model redesign

Next:

- Build MenuItem properly
- Refactor Menu
- Refactor FoodItem
- Review Cart
- Complete Cart implementation