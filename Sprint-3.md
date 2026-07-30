# Day 3 - Backend Engineering Notes
## Topic: Domain Modeling, Collections Thinking & Object Responsibilities

---

# Today's Goal

Today's objective was **not** to learn Java Collections syntax.

Instead, the focus was on learning **how backend engineers think before writing code.**

We started questioning every design decision and redesigned our classes based on real business requirements rather than simply writing Java code.

The biggest lesson of today was:

> **Good backend developers don't ask "How do I code this?"**
>
> They first ask:
>
> **"Who owns this responsibility?"**

---

# Topics Covered

- Thinking in Business Objects
- Object Responsibilities
- Composition
- Delegation
- Low Coupling
- High Cohesion
- Single Responsibility Principle (SRP)
- Domain Modeling
- API Design
- Designing before Coding
- Introduction to Java Collections (Conceptual)

---

# Biggest Mindset Shift

Instead of writing Java classes,

learn to identify

**Business Objects**

Example:

Instead of asking

"Should I create another class?"

Ask

> Does this concept have its own responsibilities?

If yes,

create a new class.

---

# Business Objects Identified

Current Domain Model

```text
Restaurant
      │
      ▼
Menu
      │
      ▼
FoodItem
```

Each object has one clear responsibility.

---

# Restaurant Responsibilities

Restaurant should only manage:

- Restaurant Name
- Address
- Rating
- Open / Close Status
- Own a Menu

Restaurant should NOT

- Manage Food Items
- Search Food
- Filter Food
- Sort Food

Those belong to Menu.

---

# Menu Responsibilities

Menu owns FoodItems.

Menu should manage:

- Add Food
- Remove Food
- Search Food
- Find Food by ID
- Find Food by Category
- Filter Veg Food
- Filter Available Food
- Sort Food

Menu is **not just a List**.

Menu is a business object.

---

# FoodItem Responsibilities

FoodItem only represents information about food.

It knows:

- ID
- Name
- Description
- Price
- Category
- Food Type
- Availability

It does NOT know:

- Restaurant
- Customer
- Order
- Quantity
- GST

---

# Composition

Restaurant

HAS-A

Menu

Menu

HAS-MANY

FoodItems

```text
Restaurant
        │
        ▼
      Menu
        │
        ▼
List<FoodItem>
```

Composition models real-world relationships.

---

# Delegation

Restaurant should not perform Menu's work.

Instead

Restaurant delegates work to Menu.

Bad

```java
restaurant.addFoodItem(food);
```

Better

```java
restaurant.getMenu()
          .addFoodItem(food);
```

Restaurant owns Menu.

Menu owns FoodItems.

Responsibilities remain clear.

---

# Why We Removed

```java
Restaurant.addFoodItem()
Restaurant.removeFoodItem()
```

Originally these methods simply forwarded calls to Menu.

Example

```java
menu.addFoodItem(foodItem);
```

Restaurant was acting as a middleman.

Since Menu owns FoodItems,

Menu should expose these behaviors directly.

This reduced unnecessary delegation and improved separation of responsibilities.

---

# Single Responsibility Principle (SRP)

Every class should have one reason to change.

Restaurant changes when

Restaurant information changes.

Menu changes when

Menu functionality changes.

FoodItem changes when

Food properties change.

This keeps the design modular.

---

# Low Coupling

Objects should know only what they need.

Good

```text
Restaurant
        │
        ▼
      Menu
        │
        ▼
    FoodItem
```

Bad

```text
Restaurant

↓

Menu

↓

FoodItem

↓

Restaurant

↓

Customer

↓

Order
```

Objects become tightly coupled.

Hard to maintain.

---

# High Cohesion

Every class should focus on one responsibility.

Restaurant

↓

Restaurant operations

Menu

↓

Menu operations

FoodItem

↓

Food operations

This creates highly cohesive classes.

---

# API Design

A backend engineer designs APIs that read like business language.

Instead of

```java
setPrice()
```

Use

```java
updatePrice()
```

Instead of

```java
setName()
```

Use

```java
renameFoodItem()
```

Instead of

```java
setAvailable(true)
```

Use

```java
markAvailable()
```

Business language makes APIs expressive.

---

# Designing Fields

Instead of asking

Should this be private?

Ask

Can business modify this?

Example

ID

Never changes.

```java
private final int id;
```

Price

Can change.

```java
private BigDecimal price;
```

Category

Normally fixed.

```java
private final FoodCategory category;
```

Availability

Changes.

```java
private boolean available;
```

Business rules determine object design.

---

# Enum vs Boolean

Instead of

```java
boolean vegetarian;
```

We introduced

```java
FoodType

VEG

NON_VEG
```

Enums communicate business language.

Compare

```java
true
```

vs

```java
FoodType.VEG
```

Enums are more readable and extensible.

---

# Enum for Category

Instead of

```java
String category;
```

We created

```java
FoodCategory
```

Benefits

- Prevents spelling mistakes
- Restricts invalid values
- Improves readability
- Better API Design

---

# Money Should Not Use double

One important realization today

Instead of

```java
double price;
```

We decided to use

```java
BigDecimal price;
```

Reason

Money requires precision.

Future calculations

- GST
- Discounts
- Coupons
- Taxes

need exact decimal arithmetic.

We will study BigDecimal in detail later.

---

# Why FoodItem Doesn't Know Restaurant

Initially we discussed

```java
private Restaurant restaurant;
```

We decided NOT to include it.

Reason

Restaurant already owns Menu.

Menu owns FoodItems.

Adding Restaurant to FoodItem creates unnecessary coupling.

Ownership already exists.

```text
Restaurant

↓

Menu

↓

FoodItem
```

FoodItem does not need to know its parent.

---

# Why FoodItem Doesn't Store Quantity

Quantity belongs to an Order.

Restaurant Menu

```text
Chicken Biryani
```

does not know

how many the customer wants.

Customer Order

```text
Chicken Biryani

Quantity = 2
```

Quantity belongs to purchasing,

not to the menu item itself.

This led us to discover a future object

```text
OrderItem
```

---

# Domain Modeling

One of the biggest lessons today

A Menu is NOT just

```java
List<FoodItem>
```

Menu has behavior.

Examples

- Search Food
- Sort Food
- Filter Food
- Find by Category
- Find by Price

If an object has behavior,

it deserves to become its own class.

---

# Avoid Over Engineering

We also discussed future possibilities.

Restaurant might later have

- Breakfast Menu
- Lunch Menu
- Dinner Menu

We intentionally decided NOT to implement

```java
List<Menu>
```

today.

Reason

Current requirement

One Restaurant

↓

One Menu

Good backend engineers solve today's problems while keeping tomorrow's changes manageable.

---

# Questions We Asked Before Coding

For every field

- Can this change?
- Who owns it?
- Is it mandatory?
- Is it optional?
- Should it be final?
- Should it be mutable?

For every method

- Why does this belong here?
- Which business rule does it enforce?
- Which object owns this behavior?

These questions are more important than Java syntax.

---

# Architecture Discussions

Before writing code we discussed

- Why Restaurant owns Menu
- Why Menu owns FoodItems
- Why FoodItem doesn't know Restaurant
- Why Quantity belongs elsewhere
- Why Money should use BigDecimal
- Why Menu should become a business object

This process mirrors real software design.

---

# Backend Engineering Mindset Learned Today

Always ask

1. Who owns this responsibility?
2. Does this concept deserve its own class?
3. Am I exposing behavior or data?
4. Can this design evolve tomorrow?
5. Am I solving today's requirement or over-engineering?

---

# Interview Questions

1. Why did you create a Menu class instead of storing List<FoodItem> inside Restaurant?

2. Why doesn't FoodItem contain Restaurant?

3. Why shouldn't Quantity belong inside FoodItem?

4. Why use Enum instead of boolean?

5. Why is Menu considered a business object?

6. What is Delegation?

7. What is Low Coupling?

8. What is High Cohesion?

9. Why did we remove addFoodItem() from Restaurant?

10. Why should money use BigDecimal instead of double?

---

# Project Structure After Day 3

```text
Restaurant
│
├── Restaurant Details
├── Status
├── Rating
└── Menu
        │
        ├── Add Food
        ├── Remove Food
        ├── Search Food
        ├── Filter Food
        └── Food Items
                │
                ├── Name
                ├── Price
                ├── Description
                ├── FoodType
                ├── FoodCategory
                └── Availability
```

---

# Key Takeaways

✔ Think in business objects, not Java classes.

✔ Every class should have one clear responsibility.

✔ Composition is preferred over unnecessary inheritance.

✔ Expose behavior, not data.

✔ Objects should own their business rules.

✔ Design first. Code later.

✔ Good APIs read like business language.

✔ Don't over-engineer for imaginary requirements.

✔ Build software that can evolve.

---

# What's Next (Day 4)

Tomorrow we move into one of the most important topics in Java and backend engineering.

## Theme:
**Collections from an Engineering Perspective**

We will explore:

- Why ArrayList exists
- Internal working of ArrayList
- Dynamic Array
- Capacity vs Size
- Resizing Mechanism
- Time Complexity
- Why LinkedList exists
- ArrayList vs LinkedList
- When to choose which
- Applying Collections to our Food Delivery project

Most importantly,

we'll continue asking

> **"Why does this collection exist?"**

instead of

> **"How do I use it?"**