# Engineering Design Review #4
## Sprint 1 - Designing the Menu Domain Object

---

# Sprint Goal

Today's goal was to continue building our Swiggy Backend while naturally learning Java Collections and object-oriented design.

Instead of jumping into Java Collections directly, we first discussed the business requirements and identified which collection best fits our domain.

The focus was not:

> "How to use ArrayList"

Instead it was:

> "Why was ArrayList invented?"

---

# Product Requirement

Restaurant owners should be able to

- Add Food Items
- Remove Food Items
- Search Food by ID
- Search Food by Name
- Display Menu
- View Available Food
- View Veg Food
- Prevent Duplicate Food IDs

These requirements naturally introduced Java Collections.

---

# Topics Covered

## Java

- Arrays
- Dynamic Arrays
- List Interface
- ArrayList
- Collections.unmodifiableList()
- Objects.requireNonNull()
- final Reference
- Programming to Interfaces

---

## OOP

- Ownership
- Encapsulation
- Information Hiding
- Composition
- Rich Domain Model

---

## SOLID

- Single Responsibility Principle
- Programming to Interface (foundation for DIP)

---

## Engineering Concepts

- Dynamic Array
- Capacity vs Size
- Mutable Objects
- Immutable References
- Read-only Views
- Defensive Programming
- API Design
- Business Rule Protection

---

# Requirement Analysis

Initially we imagined Java did not provide ArrayList.

The only available data structure was

```java
FoodItem[] foodItems = new FoodItem[10];
```

This helped us discover why dynamic collections exist.

---

# Problems with Fixed Arrays

A fixed array introduces several business problems.

## Fixed Size

Restaurant grows.

Menu grows.

Array size does not.

Eventually

```text
10 Food Items

↓

11th Food Item

↓

No Space
```

---

## Memory Wastage

Creating

```java
FoodItem[] foodItems = new FoodItem[1000];
```

for only

```text
8 Food Items
```

wastes memory.

---

## Manual Resizing

Whenever capacity becomes full

Developer has to

- Create new array
- Copy old elements
- Delete old array

This naturally led to Dynamic Arrays.

---

# Dynamic Array

We discovered the idea behind ArrayList ourselves.

When capacity becomes full

```text
Old Array

↓

Create Bigger Array

↓

Copy Elements

↓

Continue
```

This is exactly how ArrayList works internally.

---

# Why Not Increase by One?

Suppose capacity

```text
100000
```

Adding one element would require

- New Array
- Copy 100000 elements

for every insertion.

Very expensive.

---

# Why Increase by 50%?

We discussed multiple strategies.

Increase by

- 1
- 10
- 100
- Double
- 50%

Java approximately grows ArrayList by **50%**.

Reason

It balances

- Memory Usage
- Copying Cost

Engineering is always about trade-offs.

---

# Capacity vs Size

These are different concepts.

Capacity

```text
Maximum elements possible before resizing.
```

Size

```text
Current number of elements.
```

Example

Capacity

```text
10
```

Size

```text
4
```

ArrayList internally tracks both.

---

# Designing Menu

We identified Menu as a rich business object.

Menu owns

- Food Items
- Searching
- Filtering
- Adding
- Removing

Restaurant simply owns Menu.

---

# Why Menu Owns List<FoodItem>

Instead of

```java
Restaurant

↓

List<FoodItem>
```

we designed

```text
Restaurant

↓

Menu

↓

List<FoodItem>
```

Reason

Menu has its own business behavior.

---

# Field Design

Final Decision

```java
private final List<FoodItem> foodItems;
```

---

# Why private?

Only Menu should manage FoodItems.

No external class should directly modify the collection.

---

# Why final?

The reference should never change.

The contents should.

Allowed

```java
foodItems.add(food);
foodItems.remove(food);
```

Not Allowed

```java
foodItems = new LinkedList<>();
```

This protects ownership.

---

# Mutable Object vs Immutable Reference

A common misconception is

```java
final List<FoodItem>
```

makes the List immutable.

Incorrect.

The List remains mutable.

Only the reference becomes immutable.

---

# Why List Instead of ArrayList?

Instead of

```java
ArrayList<FoodItem>
```

we wrote

```java
List<FoodItem>
```

Reason

Programming to interfaces.

Tomorrow

```java
LinkedList
```

can replace

```java
ArrayList
```

without affecting other classes.

---

# Ownership Principle

Question

Who owns FoodItems?

Answer

Menu.

Therefore

Menu creates

```java
new ArrayList<>();
```

inside its constructor.

Not Main.

Not Restaurant.

---

# Why Not

```java
new Menu(new ArrayList<>());
```

Passing the collection from outside creates

Shared Mutable State.

Example

Main

↓

ArrayList

↑

Menu

Both modify the same collection.

Ownership becomes unclear.

---

# Read-only Access

Initially

```java
public List<FoodItem> getFoodItems() {
    return foodItems;
}
```

This allows

```java
menu.getFoodItems().clear();
```

Entire menu disappears.

Business rules bypassed.

---

# Defensive Programming

Instead

```java
Collections.unmodifiableList(foodItems);
```

returns a read-only view.

External code can

- Iterate
- Display

But cannot

- Add
- Remove
- Clear

Business rules remain protected.

---

# Information Hiding

Menu exposes

Read Access

Not

Write Access.

Objects should protect their own state.

---

# API Design

Instead of exposing mutable collections,

Menu exposes behavior.

Examples

```java
addFoodItem()

removeFoodItem()

findFoodById()

getAvailableFoodItems()

getVegFoodItems()
```

Objects own their operations.

---

# Code Implemented

Completed

- Menu Constructor
- Add Food
- Remove Food
- Find by ID
- Find by Name
- Get Veg Food
- Get Available Food
- Read-only Menu

---

# Engineering Principles Learned

## Principle 1

Every data structure exists because it optimizes one or more operations.

---

## Principle 2

Expose behavior whenever possible.

If data must be exposed,

expose it safely.

---

## Principle 3

Objects should protect their own invariants.

Never allow external code to bypass business rules.

---

## Principle 4

The owner creates what it owns.

Restaurant owns Menu.

Menu owns FoodItems.

Order owns OrderItems.

---

## Principle 5

Make references immutable whenever ownership never changes.

Example

```java
private final List<FoodItem> foodItems;
```

---

## Principle 6

Program to an interface,

not an implementation.

Example

```java
List<FoodItem>
```

instead of

```java
ArrayList<FoodItem>
```

---

## Principle 7

Engineering is always about balancing trade-offs.

Performance

vs

Memory

vs

Maintainability

---

# OOP Principles Applied

✔ Encapsulation

✔ Composition

✔ Information Hiding

✔ Rich Domain Model

✔ Low Coupling

✔ High Cohesion

✔ Behavior over Data

---

# SOLID Principles Introduced

✔ Single Responsibility Principle

✔ Programming to Interface (foundation for Dependency Inversion)

---

# Design Patterns / Architectural Concepts

Although no formal design pattern was implemented,

we naturally introduced

- Rich Domain Model
- Aggregate Thinking
- Defensive Programming
- Collection Encapsulation

---

# Architecture Decisions (ADR)

## ADR-006

Menu owns FoodItems.

Reason

Menu is responsible for all food-related behavior.

---

## ADR-007

Use List instead of ArrayList.

Reason

Depend on abstractions.

---

## ADR-008

Use final for collection references.

Reason

Ownership never changes.

---

## ADR-009

Expose read-only collections.

Reason

Protect business rules.

---

## ADR-010

Menu creates its own collection.

Reason

The owner creates what it owns.

---

# Mistakes We Avoided

❌ Restaurant directly managing FoodItems

❌ Returning mutable List

❌ Replacing collection reference

❌ Shared Mutable State

❌ Programming to ArrayList

❌ Exposing setters for collections

❌ Optimizing with HashMap before the problem exists

---

# Engineering Vocabulary

Dynamic Array

Capacity

Size

Ownership

Read-only View

Mutable Object

Immutable Reference

Collection Encapsulation

Information Hiding

Programming to Interface

Shared Mutable State

Trade-offs

Defensive Programming

Business Rules

Invariant

---

# Interview Questions

1. Why use List instead of ArrayList?

2. Why make the collection reference final?

3. What is the difference between an immutable object and an immutable reference?

4. Why should Menu own its collection?

5. Why not pass List into the constructor?

6. Why use Collections.unmodifiableList()?

7. What is Shared Mutable State?

8. Explain Capacity vs Size.

9. Why does ArrayList grow dynamically?

10. Why doesn't Menu expose its internal collection directly?

---

# Project Progress

Completed

✔ Restaurant

✔ FoodItem

✔ Menu

Current Domain

Restaurant

↓

Menu

↓

FoodItems

The project is no longer just a collection of Java classes.

It is slowly evolving into a production-ready backend domain model.

---

# Next Engineering Review

Customer Domain

We will introduce

- Multiple Addresses
- Address Ownership
- Value Objects
- List<Address>
- Equality
- Identity vs Value

The project continues to grow while introducing new Java concepts naturally.