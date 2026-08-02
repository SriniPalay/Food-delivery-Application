# Backend Engineering Principles

This document is continuously updated throughout the project.

---

## Principle 1

Objects should have a single responsibility.

---

## Principle 2

Objects should expose behavior, not internal state.

---

## Principle 3

Protect encapsulation.

Never expose mutable collections directly.

---

## Principle 4

Prefer composition over unnecessary coupling.

Restaurant owns Menu.

Customer owns Address.

---

## Principle 5

Objects should be born valid.

Constructor validation is preferred.

---

## Principle 6

Hide implementation details.

Expose business behavior only.

---

## Principle 7

Ownership follows business responsibility.

Customer owns addresses.

Address does not own Customer.

---

## Principle 8

If changing an object changes its meaning,
consider making it immutable.

Example

Address

Money

Coordinates

---

## Principle 9

Model today's requirements.

Don't over-engineer tomorrow's possibilities.

---

## Principle 10

Never allow an object to enter an inconsistent state.

---

## Principle 11

Good object models make invalid states difficult to represent.

---

## Principle 12

Never make important business decisions on behalf of the user.

Users should explicitly choose a new default address.

---

## Principle 13

Attach business rules to the operation that actually requires them.

Example

Customer registration

does not require an address.

Order placement

does.

---

## Principle 14

Storing multiple references is acceptable

only if the owning object guarantees consistency.

---

## Principle 15

Hide implementation details.

Expose only business behavior.

---

## Principle 16

Before making a method public ask

"Does another object actually need this?"

If not,

make it private.

---

## Principle 17

Code should communicate intent.

Method names should read like English.

Examples

validateRequiredField()

normalizeOptionalField()

changeDefaultAddress()

---

## Principle 18

Normalize optional text values.

Avoid unnecessary nulls.

---

## Principle 19

Value Objects compare values.

Entities compare identity.

---

## Principle 20

equals()

answers

"Are these logically equal?"

hashCode()

answers

"Where should Java start looking?"

---

## Principle 21

equals() and hashCode()

must always use the same fields.

---

## Principle 22

Optimize for readability before brevity.

Readable code survives production.

---

## Principle 23

Memorize principles.

Reference syntax.

Engineering knowledge is more valuable than syntax knowledge.

# Engineering Principles

---

## Principle 24

Validate objects during construction.

An object should never be created in an invalid state.

Always validate before assigning fields.

---

## Principle 25

Fail Fast.

Detect invalid input immediately and throw meaningful exceptions.

Do not allow invalid state to exist inside an object.

---

## Principle 26

Objects should be born valid.

Constructors establish valid state.

Objects should not rely on later methods to become valid.

---

## Principle 27

Normalize data before storing it.

Example

Trim strings.

Convert optional null values into sensible defaults.

This keeps object state consistent.

---

## Principle 28

Use `final` for references that should never point to another object.

Example

```java
private final List<Address> addresses;
```

The collection can change.

The reference cannot.

---

## Principle 29

If a class is designed to be immutable and there is no business need for inheritance,
consider making the class `final`.

Examples from Java

- String
- Integer
- LocalDate

---

## Principle 30

Protecting a collection is not enough.

Also consider whether the objects inside that collection should be mutable or immutable.

Protect the container.

Protect the contents.

---

## Principle 31

Do not allow extension unless there is a clear business reason.

Inheritance should solve a business problem,
not provide unnecessary flexibility.

---

## Principle 32

Don't generalize too early.

Methods that appear similar today
may evolve very differently tomorrow.

Generalize only after understanding the responsibilities.

---

## Principle 33

Generalize behavior,
not method names.

Extract only genuinely common logic.

Example

```text
validateRequiredText()

↓

validateEmail()

↓

validatePhoneNumber()
```

Shared validation belongs in one place.

Business-specific validation belongs in separate methods.

---

## Principle 34

Build for today's requirements.

Avoid enterprise-level complexity
until the application actually needs it.

Simple code today is often better than
prematurely flexible code.

---

## Principle 35

Separate generic validation
from business-specific validation.

Layer 1

Generic validation

- Null
- Blank
- Trim

Layer 2

Business validation

- Email format
- Phone number
- Name length

---

## Principle 36

Prefer expressive Collection APIs
over manual iteration.

Instead of

```java
for (...)
```

Use

```java
contains()

remove()

isEmpty()
```

These methods clearly communicate intent.

---

## Principle 37

Enforce business rules
at the point where they matter.

Example

Customer Registration

↓

No address required.

Order Placement

↓

Address required.

Avoid enforcing rules too early.

---

## Principle 38

Idempotent operations
should usually succeed quietly.

Example

Changing the default address
to the current default.

The system remains in the same valid state.

No exception is necessary.

---

## Principle 39

Use `final` classes intentionally.

Make a class `final`
only when there is no valid business reason
for inheritance.

Do not make everything final by default.

---

## Principle 40

Mutability should be decided
by the business domain,
not by Java syntax.

Ask

"Does the business expect this object to change?"

If yes

↓

Mutable

If no

↓

Immutable

---

## Principle 41

`final` and immutability solve different problems.

`final class`

↓

Prevents inheritance.

Immutable object

↓

Prevents state changes.

They are often used together,
but they are not the same concept.

---

## Principle 42

The object that owns the data
should own the operations
that modify that data.

Examples

Customer owns

```java
List<Address>
```

Therefore

```java
customer.addAddress()
```

Menu owns

```java
List<FoodItem>
```

Therefore

```java
menu.addFoodItem()
```

Ownership determines behavior.

---

## Principle 43

Introduce a new class
only when it has enough responsibilities
to justify its existence.

Do not create abstractions
simply because a class contains a collection.

Example

We chose

```java
Customer
    └── List<Address>
```

instead of

```java
Customer
    └── AddressBook
            └── List<Address>
```

because Address management is currently simple.

Create abstractions only when the business complexity demands them.