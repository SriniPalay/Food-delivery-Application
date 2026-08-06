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

## Principle 44

Don't encode business meaning
into collection order.

Bad

addresses.get(0)

means default address.

Good

Store business concepts explicitly.

private Address defaultAddress;

---

## Principle 45

Protect important state
using multiple independent layers.

Example

private

↓

final reference

↓

unmodifiableList()

↓

Immutable Address

Each layer protects the object
from a different kind of misuse.

---

## Principle 46

Entity equality should be based
on stable identity,
not mutable attributes.

Examples

Customer ID

Restaurant ID

Order ID

Never compare mutable fields
such as name or email.

---

## Principle 47

Implement equals() and hashCode()
so that Java Collections Framework
can correctly work with your objects.

Collections such as

contains()

remove()

HashSet

HashMap

depend on them.

---

## Principle 48

Consistency is more important
than personal preference.

Whether your team always uses

this.field

or omits

this

be consistent across the codebase.

---

## Principle 49

toString() is for debugging,
not for exposing business data.

Avoid logging sensitive information
such as email,
phone number,
or passwords.

---

## Principle 50

If an object owns another object
and that owned object has
a sensible default state,

let the owner create it.

Examples

Customer creates Address List.

Restaurant creates Menu.

---

## Principle 51

Never create an object
that another object
is already responsible for creating.

Main.java should never create Menu
if Restaurant already owns it.

---

## Principle 52

The owner of an object
should own its lifecycle.

Creation

Modification

Deletion

should normally be controlled
by the owner.

---

## Principle 53

Demo code should tell
a business story,
not merely execute methods.

A reader should understand
the business flow
without reading comments.

---

## Principle 54

A demo application should validate

both

successful scenarios

and

failure scenarios.

Business rules are only trusted
when both paths are tested.

---

## Principle 55

Variable names should describe
their business role,
not merely their data type.

Good

restaurantAddress

homeAddress

officeAddress

Bad

address

address1

address2

---

## Principle 56

Main.java should orchestrate
the application,
not contain business logic.

Create

↓

Call

↓

Display

Nothing more.

---

## Principle 57

Separate object creation
from object association.

Example

Create FoodItem

↓

Add FoodItem to Menu

These are different business events.

---

## Principle 58

Do not expose mutable
data structures.

Expose behaviour instead.

Good

menu.addFoodItem()

customer.addAddress()

Bad

menu.getFoodItems().add(...)

customer.getAddresses().add(...)

---

## Principle 59

Expose behaviours,
not implementation details.

External objects should ask
the owner to perform an operation.

They should never manipulate
internal collections directly.

# Engineering Principles

---

## Principle #60 — Business Rules Should Drive Object Design

Never create fields or classes because Java allows it.

Create them because the business requires them.

Bad:

"I'll add a Restaurant field."

Good:

"The cart must enforce a one-restaurant rule, therefore it needs to know its restaurant."

Business requirements should shape the object model.

---

## Principle #61 — Group Related Information into an Object

When multiple pieces of information naturally belong together, model them as a separate object.

Example:

Instead of:

- FoodItem
- Quantity
- Instructions
- Subtotal

stored separately,

create:

CartItem

Objects represent concepts.

Collections store objects.

---

## Principle #62 — Think from the Business, Not the Data Structure

Choose List, Set, or Map based on the business model—not because a particular collection seems convenient.

Bad:

Map<FoodItem, Integer>

Good:

List<CartItem>

If a Map starts accumulating metadata, it's often a sign that another class should exist.

---

## Principle #63 — Repeated Business Concepts Deserve Their Own Class

Whenever a business concept repeats independently, it usually deserves its own class.

Examples:

Cart
↓
CartItem

Order
↓
OrderItem

Invoice
↓
InvoiceLine

Playlist
↓
PlaylistSong

---

## Principle #64 — Hide Internal Implementation Details

Public APIs should expose business operations.

Avoid exposing implementation-specific objects.

Good:

cart.addFoodItem(foodItem);

Avoid:

cart.addCartItem(cartItem);

The caller should think in terms of business actions, not internal implementation.

---

## Principle #65 — Object Lifecycle Determines Ownership

Ask:

"Can this object exist without its parent?"

If YES

Independent lifecycle.

Created externally.

Examples:

- Address
- FoodItem
- Customer
- Restaurant

If NO

Owned lifecycle.

Created internally.

Examples:

- Menu
- CartItem

Ownership determines creation.

---

## Principle #66 — Repeated Business Concepts Deserve Their Own Class

Whenever one object contains many repeated business entries, the repeated entry usually deserves its own class.

Examples:

- Cart → CartItem
- Order → OrderItem
- Invoice → InvoiceLine
- Playlist → PlaylistSong

Why?

Because each repeated object eventually grows its own state and behavior.

Instead of forcing multiple collections or maps, model the repeated business concept as an object.

---

## Principle #67 — Prefer Unidirectional Relationships

Do not make two objects reference each other unless both genuinely need collaboration.

Good:

Customer
↓
Cart

Restaurant
↓
Menu

Bad:

Customer ↔ Cart

unless Cart actually needs Customer.

Benefits:

- Lower coupling
- Easier reasoning
- Fewer synchronization bugs

---

## Principle #68 — One Owner, One Creator

Every owned object should have one obvious place where it is created.

Examples:

Restaurant creates Menu.

Customer creates Cart.

Cart creates CartItem.

Ownership implies creation.

---

## Principle #69 — Store Business Concepts Explicitly

If a field represents an important business concept, store it explicitly rather than deriving it from other data.

Example:

Store:

private Restaurant restaurant;

instead of

cartItems.get(0).getRestaurant()

Business concepts should be immediately visible.

---

## Principle #70 — Freeze Business Rules Before Designing

Never design classes before agreeing on the business rules.

Example:

Business Rule:

One Cart → One Restaurant

Once this rule is fixed, the class design becomes obvious.

Changing business rules changes architecture.

---

## Principle #71 — Store Facts, Derive Calculations

Store facts.

Derive calculations.

Store:

- Restaurant
- Quantity
- Status

Derive:

- Total Price
- Tax
- Discounts

Calculated values should not be stored unless necessary for performance.

---

## Principle #72 — Don't Express Java Defaults

Avoid writing code that only repeats Java's default behavior.

Avoid:

this.restaurant = null;

this.quantity = 0;

unless those assignments communicate business meaning.

Less code means less maintenance.

---

## Principle #73 — Backend Validates, Frontend Communicates

Backend responsibility:

- Validate business rules
- Reject invalid operations

Frontend responsibility:

- Interact with the user
- Decide how to present errors or confirmations

Example:

Backend:

throw new IllegalStateException(...)

Frontend:

"Replace Cart?"

Never let the backend silently perform destructive actions.

---

## Principle #74 — Never Expose Mutable Collections

Always expose read-only views of collections.

Good:

Collections.unmodifiableList(...)

Avoid exposing internal collections directly.

The owning object should remain responsible for maintaining its business invariants.

---

## Principle #75 — Refactor When the Domain Model Breaks

If the domain model starts feeling unnatural,

Stop.

Fix the model before adding more features.

Today's example:

Instead of

Restaurant
↓
Menu
↓
FoodItem

we evolved to

Restaurant
↓
Menu
↓
MenuItem
↓
FoodItem

Good engineers improve the model rather than adding workarounds.

## Principle #76 — Constructors Should Reuse Business Methods

If a constructor and a business method perform the same validation, reuse the business method instead of duplicating logic.

Example:

public MenuItem(...) {
updatePrice(price);
}

instead of

this.price = validate(price);

Benefits:

- One validation
- One maintenance point
- No duplicated business rules

---

## Principle #77 — Separate Commands from Queries

A method should either:

- Modify state (Command)

OR

- Return information (Query)

Commands:

- updatePrice()
- increaseQuantity()
- decreaseQuantity()
- addAddress()

Queries:

- getPrice()
- getSubtotal()
- getQuantity()
- isAvailable()

Keeping commands and queries separate makes APIs easier to understand.

---

## Principle #78 — Prefer Intention-Revealing Methods

Avoid generic setters.

Good:

markAvailable()

increaseQuantity()

renameFoodItem()

checkout()

Avoid:

setAvailable(true)

setQuantity(5)

Business methods communicate intent.

---

## Principle #79 — Normalize During Every State Change

If a value is normalized during object creation, it must also be normalized whenever it changes.

Example:

Constructor

this.name = normalize(name);

Rename

this.name = normalize(newName);

Objects should never exist in multiple formats.

---

## Principle #80 — Good Refactoring Makes Classes Simpler

Introducing a new class should usually reduce responsibilities in existing classes.

Example:

FoodItem

Before:

- Name
- Price
- Availability

After introducing MenuItem:

- Name
- FoodType
- Category

Responsibilities moved to the object that naturally owns them.

---

## Principle #81 — Search Entities by Identity

Entities should be searched using stable identifiers.

Good:

foodItemId

customerId

orderId

Avoid:

foodName

customerName

Names change.

IDs do not.

---

## Principle #82 — Don't Implement Methods Without Business Need

Never override equals(), hashCode(), or add helper methods simply because "every class has them."

Ask:

"Does the business require this behavior?"

If the answer is no,

don't implement it.

---

## Principle #83 — Generalize Only After Multiple Use Cases

Don't build generic frameworks because they might be useful later.

Start with clear business methods.

Example:

getVegMenuItems()

Only generalize to filtering engines after multiple real business requirements appear.

YAGNI (You Aren't Gonna Need It).

---

## Principle #84 — Owned Objects Without Identity Are Value Objects

If an object

- has no independent lifecycle,
- has no independent identity,
- cannot exist outside its owner,

it is usually a Value Object.

Examples:

Address

CartItem

OrderLine

InvoiceLine

---

## Principle #85 — Constructors Create Valid Objects

Constructors establish the first valid state.

Business methods evolve the object afterward.

Example:

CartItem starts with

quantity = 1

instead of accepting arbitrary quantities.

---

## Principle #86 — Every Object Protects Its Own Invariants

Objects should never allow themselves to enter an invalid state.

Example:

CartItem never allows

quantity < 1

Instead of trusting callers,

the object protects itself.

---

## Principle #87 — Never Pass an Object Its Own Data

If an object already owns the information,

don't pass that same information back as parameters.

Bad:

cartItem.getSubtotal(price, quantity);

Good:

cartItem.getSubtotal();

Objects should use their own state whenever possible.

---

## Principle #88 — Tell, Don't Ask

Instead of asking an object for its data and doing the work elsewhere,

tell the object what you want.

Bad:

price = cartItem.getMenuItem().getPrice();

quantity = cartItem.getQuantity();

calculate(price, quantity);

Good:

cartItem.getSubtotal();

Behavior belongs with the data.