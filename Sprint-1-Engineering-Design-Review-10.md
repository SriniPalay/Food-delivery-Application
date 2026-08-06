# Sprint 1 — Day 10

## Theme

Better object models produce simpler code.

Today's focus was completing the Menu refactoring before continuing Cart.

---

# Objectives

- Design MenuItem
- Refactor FoodItem
- Refactor Menu
- Begin CartItem

---

# MenuItem

Final fields

- FoodItem
- Price
- Availability

MenuItem represents the restaurant-specific version of a FoodItem.

---

# FoodItem

FoodItem now represents only the catalog item.

Responsibilities

- Name
- FoodType
- FoodCategory

Price removed.

Restaurant removed.

FoodItem became significantly simpler after introducing MenuItem.

---

# Menu

Menu now owns MenuItems instead of FoodItems.

Implemented

- addMenuItem()
- removeMenuItem()
- getMenuItems()
- getAvailableMenuItems()
- getVegMenuItems()

Searches are now based on FoodItem IDs.

---

# CartItem

Designed as an owned object of Cart.

Fields

- MenuItem
- Quantity

Business operations

- increaseQuantity()
- decreaseQuantity()
- getSubtotal()

Constructor always initializes quantity to one.

---

# Major Design Discussions

## Command vs Query

Commands modify state.

Queries return information.

Methods should avoid doing both.

---

## Tell, Don't Ask

Objects should perform operations using their own data.

Example

cartItem.getSubtotal()

instead of

cartItem.getSubtotal(price, quantity)

---

## Responsibility Distribution

FoodItem

↓

MenuItem

↓

CartItem

↓

Cart

Each object owns only the behavior directly related to its data.

---

# Progress

Completed

✅ MenuItem

✅ FoodItem

✅ Menu

🟡 CartItem (almost complete)

Remaining

⬜ Cart

⬜ Main refactor

⬜ Integration testing

---

# Biggest Learning

Today's biggest improvement was not writing more code.

It was reducing responsibilities.

Every refactoring made the previous classes smaller, simpler, and more focused.

That is one of the strongest indicators of a healthy object-oriented design.