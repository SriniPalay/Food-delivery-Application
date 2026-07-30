# Food Delivery Application - Backend Architecture [cite: 2]

This repository tracks my 6-month journey toward becoming a Backend Engineer using Java, Spring Boot, and modern backend technologies [cite: 2]. The journey begins with mastering Core Java, Object-Oriented Programming (OOP), and Low-Level Design (LLD) before progressing to Spring Boot, databases, caching, messaging systems, Docker, CI/CD, and cloud deployment [cite: 2].

## Day 1: Core System Modeling [cite: 2]

### Focus [cite: 2]
Today's objective was to model the core entities of a Swiggy-like food delivery platform using pure Java [cite: 2].
* No fancy algorithms [cite: 2]
* No database [cite: 2]
* No Spring Boot (yet) [cite: 2]
* No frameworks [cite: 2]
* Pure Java, OOP, and LLD [cite: 2]

The emphasis is on building a strong foundation by designing clean, extensible, and maintainable object models [cite: 2].

### Learning Objectives [cite: 2]
During Day 1, the following concepts were explored: [cite: 2]
* Identifying domain entities [cite: 2]
* Composition over inheritance [cite: 2]
* Encapsulation [cite: 2]
* Class responsibilities [cite: 2]
* Entity relationships [cite: 2]
* Java packages [cite: 2]
* Enums [cite: 2]
* Immutable vs mutable objects [cite: 2]
* Basic project organization [cite: 2]

### Project Structure [cite: 2]
```text
Food-delivery-Application
└── src
    └── main
        └── java
            └── org.swiggy.models
                ├── Address.java
                ├── Customer.java
                ├── FoodItem.java
                ├── Main.java
                ├── Menu.java
                ├── Order.java
                ├── OrderStatus.java
                ├── PaymentType.java
                └── Restaurant.java
```

### Domain Model [cite: 2]

**Restaurant** [cite: 2]
Represents a restaurant registered on the platform [cite: 2].
Responsibilities:
* Store restaurant details [cite: 2]
* Own a menu [cite: 2]
* Maintain address information [cite: 2]
* Maintain restaurant rating [cite: 2]

**Menu** [cite: 2]
Represents the list of food items offered by a restaurant [cite: 2].
Responsibilities:
* Store available food items [cite: 2]
* Provide access to menu items [cite: 2]
* Allow future extensions such as categories and availability [cite: 2]

**FoodItem** [cite: 2]
Represents an individual food item [cite: 2].
Attributes include:
* Name [cite: 2]
* Price [cite: 2]
* Description [cite: 2]
* Availability (future enhancement) [cite: 2]

**Customer** [cite: 2]
Represents an application user who can place orders [cite: 2].
Responsibilities:
* Store customer details [cite: 2]
* Maintain delivery address [cite: 2]
* Place orders [cite: 2]

**Order** [cite: 2]
Represents a food order placed by a customer [cite: 2].
Contains:
* Ordered food items [cite: 2]
* Order status [cite: 2]
* Payment type [cite: 2]
* Customer [cite: 2]
* Restaurant [cite: 2]

**Address** [cite: 2]
A reusable value object shared by multiple entities [cite: 2].
Used by:
* Customer [cite: 2]
* Restaurant [cite: 2]

### Enumerations [cite: 2]

**OrderStatus** [cite: 2]
Represents the lifecycle of an order [cite: 2].
Example values:
* PLACED [cite: 2]
* ACCEPTED [cite: 2]
* PREPARING [cite: 2]
* OUT_FOR_DELIVERY [cite: 2]
* DELIVERED [cite: 2]
* CANCELLED [cite: 2]

**PaymentType** [cite: 2]
Represents supported payment methods [cite: 2].
Example values:
* CASH [cite: 2]
* CARD [cite: 2]
* UPI [cite: 2]
* NET_BANKING [cite: 2]

### Design Principles Followed [cite: 2]
* Composition over inheritance [cite: 2]
* High cohesion [cite: 2]
* Low coupling [cite: 2]
* Encapsulation [cite: 2]
* Separation of concerns [cite: 2]
* Clean package organization [cite: 2]

# Day 2 - Constructors, Encapsulation & Object-Oriented Design

---

# Objective

After identifying the core domain entities on Day 1, today's objective was to transform those entities into **well-designed Java objects**.

Instead of simply creating classes with fields and generating getters/setters, the focus was on designing objects that protect their own state and represent real business entities.

The goal was to understand:

- Why constructors exist
- Why encapsulation is one of the pillars of OOP
- How objects should be created
- How to design meaningful business methods
- When and why to use the `final` keyword

---

# Topics Covered

- Constructors
- Encapsulation
- Object Creation
- Business Method Design
- Immutable vs Mutable Objects
- Introduction to `final`
- Composition over Data Exposure

---

# Learning Outcomes

By the end of Day 2 I learned how to:

- Design valid objects
- Prevent invalid object creation
- Protect internal object state
- Expose behavior instead of data
- Identify immutable fields
- Decide where `final` should be used
- Write classes that are easier to maintain and extend

---

# Why Constructors Exist

A constructor ensures that an object is created in a valid state.

Instead of allowing this:

```java
Restaurant restaurant = new Restaurant();

restaurant.setName("Paradise");
restaurant.setAddress(address);
```

We force mandatory information during creation.

```java
Restaurant restaurant =
        new Restaurant(
                id,
                "Paradise",
                address
        );
```

This guarantees that every `Restaurant` object starts with the minimum required information.

---

# Mandatory vs Optional Fields

One important exercise was identifying which fields are mandatory and which are optional.

## Restaurant

| Field | Mandatory | Mutable |
|--------|-----------|----------|
| id | ✅ | ❌ |
| name | ✅ | ❌ |
| address | ✅ | Depends on business |
| menu | Automatically created | Internal |
| rating | ❌ | ✅ |
| open | Default = true | ✅ |

---

## Customer

| Field | Mandatory | Mutable |
|--------|-----------|----------|
| id | ✅ | ❌ |
| name | ✅ | ❌ |
| email | ✅ | ✅ |
| phone | ✅ | ✅ |
| addresses | Empty List | ✅ |

---

## FoodItem

| Field | Mandatory | Mutable |
|--------|-----------|----------|
| id | ✅ | ❌ |
| name | ✅ | ❌ |
| price | ✅ | Depends on business |
| veg | ✅ | ❌ |

---

# Encapsulation

Encapsulation is **not simply making fields private**.

Its real purpose is:

> Protect the object's internal state by allowing controlled modifications.

Instead of exposing setters for every field,

Avoid

```java
restaurant.setRating(-100);
```

Use meaningful business methods.

```java
restaurant.updateRating(4.5);
```

Inside the method we validate the business rules.

```java
if(rating < 0 || rating > 5){
    throw new IllegalArgumentException();
}
```

The object protects itself.

---

# Business Methods Instead of Generic Setters

Rather than exposing setters for every field, classes expose business operations.

## Restaurant

```java
addFoodItem()

removeFoodItem()

updateRating()

openRestaurant()

closeRestaurant()
```

---

## Customer

```java
addAddress()

changeEmail()

changePhone()
```

---

## Order

```java
addFoodItem()

calculateTotal()

cancel()
```

This approach makes the code expressive and prevents invalid state changes.

---

# Composition

The project follows **Composition over Inheritance**.

Example:

A Restaurant **HAS A** Menu.

```java
private final Menu menu;
```

Not

```java
class Restaurant extends Menu
```

Composition models real-world relationships more accurately and keeps the design flexible.

---

# Introduction to final

One of the biggest concepts learned today was understanding the `final` keyword.

## final Variables

`final` prevents a variable from being reassigned.

```java
private final int id;
```

An object's identity never changes.

---

## final Object References

```java
private final Menu menu;
```

The restaurant will always own the same menu object.

The menu contents may change.

The menu reference cannot.

```java
menu.addFoodItem(item);      // Allowed

menu = new Menu();           // Not Allowed
```

---

## When to Use final

Use `final` when the reference should never change.

Examples:

```java
id

menu

foodItems

customer

restaurant
```

Avoid `final` for fields that naturally change.

Examples:

```java
rating

status

email

phone

isOpen
```

---

# Mutable vs Immutable Objects

## Immutable

Examples

- id
- FoodItem name
- Address

These values normally never change after creation.

---

## Mutable

Examples

- Order Status
- Rating
- Phone Number
- Email

These values change as part of business operations.

---

# Collections and Encapsulation

Instead of exposing internal collections directly,

Avoid

```java
return foodItems;
```

Return an unmodifiable collection.

```java
return Collections.unmodifiableList(foodItems);
```

This prevents external classes from modifying internal state.

---

# Project Structure

```text
org.swiggy
│
├── model
│   ├── Address.java
│   ├── Customer.java
│   ├── FoodItem.java
│   ├── Menu.java
│   ├── Order.java
│   └── Restaurant.java
│
├── enums
│   ├── OrderStatus.java
│   └── PaymentType.java
│
└── Main.java
```

---

# Design Principles Learned

- Encapsulation
- Composition over Inheritance
- Single Responsibility Principle (Introduction)
- Information Hiding
- Object Responsibility
- Domain Modeling

---

# Key Takeaways

✔ Constructors should enforce valid object creation.

✔ Objects should protect their own state.

✔ Business methods are better than exposing setters.

✔ `final` protects object references and identity.

✔ Composition models relationships better than inheritance.

✔ Good object-oriented design focuses on behavior, not just data.

---

# Current Domain Model

```text
Customer
    │
    │ places
    ▼
Order
    │
    │ contains
    ▼
FoodItem
    ▲
    │
belongs to
    │
Restaurant
    │
    │ has
    ▼
Menu
```

---

# What's Next (Day 3)

Tomorrow the focus shifts from object design to **Java Collections Framework**.

Topics include:

- ArrayList
- LinkedList
- HashSet
- HashMap
- equals()
- hashCode()
- Why collections require proper object equality
- Choosing the correct collection based on business requirements
- Real-world collection usage inside our Food Delivery Application

We'll also begin implementing richer business logic using the models designed during Days 1 and 2.

### Long-Term Roadmap [cite: 2]
This project will gradually evolve into a production-style backend by incorporating: [cite: 2]
* Core Java [cite: 2]
* Java Collections Framework [cite: 2]
* Multithreading [cite: 2]
* Design Patterns [cite: 2]
* SOLID Principles [cite: 2]
* Spring Boot [cite: 2]
* REST APIs [cite: 2]
* PostgreSQL [cite: 2]
* Redis [cite: 2]
* Kafka [cite: 2]
* Docker [cite: 2]
* Git & GitHub [cite: 2]
* GitHub Actions (CI/CD) [cite: 2]
* AWS Deployment [cite: 2]
* Unit & Integration Testing [cite: 2]
* Microservices [cite: 2]
* System Design [cite: 2]