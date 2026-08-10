# Sprint 1 - Day 7

# Main.java Integration & Customer PR Review

---

# Goal

Review Customer.java like a production Pull Request.

Build the first runnable application by integrating all domain objects
through Main.java.

Discussion : 70%

Implementation : 30%

---

# Customer Pull Request Review

Today's focus was not fixing bugs.

Instead, we reviewed the design decisions.

Topics reviewed

- defaultAddress design
- equals()
- hashCode()
- Getters
- toString()
- Encapsulation
- Collection exposure

Customer.java is now considered Version 1.0.

Future modifications should happen only when business
requirements change.

---

# defaultAddress Design

Decision

Keep

private Address defaultAddress;

instead of relying on

addresses.get(0)

Reason

Collection order should never carry business meaning.

Sorting addresses should not accidentally change
the default address.

---

# equals()

Address

↓

Equality by values.

Customer

↓

Equality by identity.

Reason

Customer is an Entity.

Address is a Value Object.

---

# Why override equals()

Java's default implementation compares memory addresses.

Our implementation teaches Java what "equal" means
for Customer.

Collections such as

- contains()
- remove()
- HashSet
- HashMap

all rely on equals().

---

# We realised something important

Customer.equals()

is not currently used inside our application.

It exists so that Java Collections Framework
can correctly compare Customer objects
when future features are added.

---

# Main.java Design

Main.java is not business logic.

Main.java is

- Demo Application
- Manual Integration Test
- Learning Playground

Its responsibility is

Create Objects

↓

Call Business Methods

↓

Display Results

It should never contain business rules.

---

# Business Story

Main.java should read like a business journey.

Restaurant joins Swiggy

↓

Food Items created

↓

Menu populated

↓

Customer registers

↓

Addresses added

↓

Business rules validated

↓

Application state displayed

---

# Menu Ownership

Restaurant owns Menu.

Therefore

Restaurant creates Menu.

Main.java should never create another Menu.

Correct

Restaurant

↓

creates Menu

↓

Menu manages FoodItems

---

# Food Item Lifecycle

Decision

Create FoodItem objects first.

Later associate them with Menu.

Reason

Creation and Association
are different business events.

FoodItem can exist
before it belongs to a Menu.

---

# Collection Protection

We discussed why

paradise.getMenu().addFoodItem(...)

is allowed

while

customer.getAddresses().add(...)

is not.

Reason

Menu exposes behaviour.

Customer never exposes
its internal collection.

The collection remains protected.

---

# Main.java Improvements

- Better variable names
- Separate Restaurant and Customer addresses
- Use BigDecimal.valueOf()
- Test failure scenarios
- Improve console readability
- Remove unnecessary Menu creation

---

# Biggest Learning

Mutable objects are not dangerous.

Uncontrolled mutation is.

Expose behaviour.

Never expose internal mutable collections.