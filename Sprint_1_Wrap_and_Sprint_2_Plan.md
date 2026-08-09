# Sprint 1 --- Swiggy LLD Wrap-Up: Day 11 and 12

## Sprint Goal

Build the first version of a Swiggy-style domain model while learning
object-oriented design, ownership, encapsulation, invariants, and clean
API design.

The goal was not simply to make the program work. The goal was to learn
how to decide:

-   Where data belongs
-   Who owns an object
-   Who creates an object
-   Who is allowed to mutate state
-   Where business rules should live
-   What should be public vs private
-   When to use composition instead of inheritance
-   How to distinguish Entities from Value Objects

------------------------------------------------------------------------

# 1. Domain Model at the End of Sprint 1

``` text
Customer
 ├── Address(es)
 └── Cart
       └── CartItem(s)
             └── MenuItem
                   ├── FoodItem
                   └── Restaurant

Restaurant
 └── Menu
       └── MenuItem
```

The important relationships are:

``` text
Customer ──owns──> Cart
Cart ──owns──> CartItem
CartItem ──references──> MenuItem
MenuItem ──references──> FoodItem
MenuItem ──references──> Restaurant
Restaurant ──owns──> Menu
Menu ──owns──> MenuItem
Customer ──owns──> Address(es)
```

------------------------------------------------------------------------

# 2. Classes Covered

## Address

Represents a customer's saved address.

Key learning:

-   Value Object thinking
-   Immutability
-   Validation
-   `equals()` / `hashCode()`
-   Defensive collection exposure

------------------------------------------------------------------------

## Customer

Represents the customer.

Responsibilities:

-   Maintain customer identity/details
-   Own saved addresses
-   Own the customer's Cart

Important decision:

``` text
Customer creates its Cart.
```

Customer does not expose:

``` text
setCart()
createCart()
```

The Cart exists even when empty.

------------------------------------------------------------------------

## FoodItem

Represents the food itself.

``` text
FoodItem
├── foodItemId
├── name
├── foodType
└── category
```

Important distinction:

> FoodItem describes WHAT the food is.

Price and restaurant-specific availability do not belong here.

------------------------------------------------------------------------

## MenuItem

Represents a restaurant's offering of a FoodItem.

``` text
MenuItem
├── FoodItem
├── Restaurant
├── price
└── availability
```

Important distinction:

> MenuItem describes HOW a restaurant offers the food.

This separation allows:

``` text
Same FoodItem
    ├── Restaurant A → ₹400
    ├── Restaurant B → ₹350
    └── Restaurant C → ₹450
```

------------------------------------------------------------------------

## Menu

Owns the restaurant's MenuItems.

Responsibilities:

-   Add MenuItems
-   Remove MenuItems
-   Return MenuItems
-   Return available MenuItems
-   Return vegetarian MenuItems
-   Prevent duplicate FoodItems in the same Menu

Important principle:

> The owner of a collection should control mutations to that collection.

Therefore:

``` java
menu.addMenuItem(item);
menu.removeMenuItem(id);
```

is preferred over directly modifying the internal list.

------------------------------------------------------------------------

## CartItem

Represents one selected MenuItem and its quantity.

``` text
CartItem
├── MenuItem
└── quantity
```

Responsibilities:

-   Maintain quantity
-   Increase quantity
-   Decrease quantity
-   Calculate subtotal

Important decision:

``` java
cartItem.getSubtotal();
```

instead of making Cart calculate:

``` text
price × quantity
```

This demonstrates Tell, Don't Ask.

------------------------------------------------------------------------

## Cart

Represents the customer's current shopping cart.

Responsibilities:

-   Own CartItems
-   Add MenuItems
-   Remove MenuItems
-   Clear the cart
-   Calculate total
-   Enforce the one-restaurant rule
-   Maintain current restaurant

Important business invariant:

> A Cart cannot contain MenuItems from multiple Restaurants.

The Cart creates CartItems:

``` java
cartItems.add(new CartItem(menuItem));
```

------------------------------------------------------------------------

## Main

Main is the integration/demo entry point.

It should create the initial domain objects and exercise the system.

It should NOT become responsible for maintaining business rules or
manipulating internal collections.

------------------------------------------------------------------------

# 3. Major Design Decisions

## FoodItem vs MenuItem

We deliberately separated:

``` text
FoodItem = food identity/details
MenuItem = restaurant-specific offering
```

This prevents restaurant-specific data such as price from leaking into
FoodItem.

------------------------------------------------------------------------

## Cart vs CartItem

We separated:

``` text
Cart = collection + cart-level rules
CartItem = one selected item + quantity
```

Cart owns CartItems.

CartItem owns its quantity.

------------------------------------------------------------------------

## Customer vs Cart

We decided:

``` text
Customer owns Cart.
Customer creates Cart.
```

Main does not create and inject a Cart.

------------------------------------------------------------------------

## Restaurant vs Menu

We decided:

``` text
Restaurant owns Menu.
Menu owns its MenuItems.
```

Menu controls mutation of its collection.

------------------------------------------------------------------------

## Restaurant inside MenuItem

We considered:

``` java
private final int restaurantId;
```

versus:

``` java
private final Restaurant restaurant;
```

We chose the Restaurant object because the relationship is meaningful to
the current domain and Cart needs restaurant identity to enforce its
invariant.

This is a conscious coupling decision, not an accidental one.

------------------------------------------------------------------------

# 4. Encapsulation Lessons

We repeatedly protected internal state.

Instead of:

``` java
cart.getCartItems().clear();
```

the caller should use:

``` java
cart.clearCart();
```

Instead of:

``` java
cartItem.setQuantity(-5);
```

we use:

``` java
cartItem.increaseQuantity();
cartItem.decreaseQuantity();
```

Instead of:

``` java
menu.getMenuItems().add(item);
```

we use:

``` java
menu.addMenuItem(item);
```

Collections are exposed using:

``` java
Collections.unmodifiableList(...)
```

This allows read access without giving callers mutation access.

------------------------------------------------------------------------

# 5. Entity vs Value Object

Entities:

-   Customer
-   Restaurant
-   FoodItem
-   MenuItem
-   Cart

These have meaningful identity.

Value Object:

-   Address

Address is defined primarily by its values rather than independent
identity.

Important rule:

> Do not add `equals()` and `hashCode()` mechanically to every class.
> Decide based on domain semantics.

------------------------------------------------------------------------

# 6. Composition vs Inheritance

We used composition extensively.

Examples:

``` text
Customer HAS Cart
Cart HAS CartItems
CartItem HAS MenuItem
MenuItem HAS FoodItem
Restaurant HAS Menu
Customer HAS Addresses
```

We intentionally did not use:

``` java
class MenuItem extends FoodItem
```

because a MenuItem is not a FoodItem.

Likewise:

``` java
class CartItem extends MenuItem
```

would be incorrect.

Core principle:

> Prefer composition when the relationship is HAS-A. Use inheritance
> when there is a genuine IS-A/substitutability relationship.

------------------------------------------------------------------------

# 7. Money

We use:

``` java
BigDecimal
```

instead of:

``` java
double
```

for monetary values.

Examples:

``` java
BigDecimal.valueOf(400)
BigDecimal.ZERO
```

This avoids common floating-point precision problems.

------------------------------------------------------------------------

# 8. Validation

Objects validate themselves at construction or mutation boundaries.

Examples:

``` java
Objects.requireNonNull(...)
```

and validation such as:

``` java
if (id <= 0)
```

and:

``` java
if (name.isBlank())
```

Principle:

> Do not allow invalid objects to enter the system.

------------------------------------------------------------------------

# 9. Command vs Query

Commands change state:

``` text
addMenuItem()
removeMenuItem()
clearCart()
increaseQuantity()
decreaseQuantity()
renameFoodItem()
updatePrice()
```

Queries read state:

``` text
getRestaurant()
getCartItems()
getTotal()
getSubtotal()
getMenuItems()
isEmpty()
isAvailable()
```

Keeping this distinction clear makes APIs easier to understand.

------------------------------------------------------------------------

# 10. Tell, Don't Ask

Instead of pulling data out of an object and performing its behavior
elsewhere:

``` text
get price
get quantity
calculate subtotal
```

we use:

``` java
cartItem.getSubtotal();
```

The object that owns the data performs the operation that naturally
belongs to it.

------------------------------------------------------------------------

# 11. Ownership and Object Creation

The major ownership chain is:

``` text
Customer → creates Cart
Cart → creates CartItem
Restaurant → owns Menu
Menu → manages MenuItems
```

This prevents Main from becoming a God Object.

General principle:

> Object creation should normally follow ownership/lifecycle
> responsibility.

------------------------------------------------------------------------

# 12. Senior Engineer PR Review

## 🔴 Actual Design Problems

### 1. Raw collection typing

Bad:

``` java
private final List cartItems;
```

Correct:

``` java
private final List<CartItem> cartItems;
```

### 2. Availability business-rule gap

Cart should eventually reject unavailable MenuItems.

Potential rule:

``` java
if (!menuItem.isAvailable()) {
    throw new IllegalStateException(
        "Menu item is currently unavailable."
    );
}
```

This is an important domain invariant to address.

### 3. API naming consistency

Use consistent naming such as:

``` text
getSubtotal()
addMenuItem()
removeMenuItem()
getFoodItemId()
```

Avoid inconsistent variants such as:

``` text
getSubTotal()
addFoodItem(MenuItem)
```

------------------------------------------------------------------------

# 13. 🟡 Things Worth Improving

These are not necessarily blockers.

### Stronger ID types

Currently:

``` java
int customerId
int restaurantId
int foodItemId
```

A larger production system could use:

``` text
CustomerId
RestaurantId
FoodItemId
OrderId
```

to provide stronger type safety.

### Diagnostic `toString()`

Current output is verbose because objects contain other objects.

A cleaner diagnostic representation could be:

``` text
Restaurant{id=1, name='Paradise'}
MenuItem{food='Mushroom Biryani', price=400}
CartItem{food='Mushroom Biryani', quantity=2}
```

### Restaurant coupling

We consciously chose:

``` java
private final Restaurant restaurant;
```

inside MenuItem.

This is acceptable for the current model but is a coupling trade-off
worth remembering.

### Price snapshot

Current structure:

``` text
CartItem → MenuItem → price
```

raises an important question:

> What happens if the restaurant changes the MenuItem price after the
> customer adds it to the cart?

We will revisit this when designing Checkout and Order.

### Availability transitions

Eventually we may prefer domain operations such as:

``` text
markAvailable()
markUnavailable()
```

rather than unrestricted setters.

------------------------------------------------------------------------

# 14. 🟢 Things We Should Leave Alone

Do not refactor these just for the sake of refactoring:

-   Customer owning Cart
-   Cart owning CartItems
-   Restaurant owning Menu
-   Menu owning MenuItems
-   MenuItem composing FoodItem
-   Composition over inheritance
-   `final` fields
-   `Collections.unmodifiableList()`
-   `Objects.requireNonNull()`
-   `BigDecimal`
-   Quantity methods
-   `getSubtotal()`
-   Private search helpers
-   ID-based identity
-   Minimal public APIs
-   No unnecessary setters
-   No unnecessary services
-   No unnecessary repositories
-   No unnecessary design patterns

Important principle:

> A senior engineer does not maximize abstraction. A senior engineer
> maximizes appropriate design.

------------------------------------------------------------------------

# 15. Sprint 1 Interview Principles

These are the principles to retain for interviews.

1.  Objects should protect their own invariants.
2.  The owner should control its collection.
3.  Do not expose mutable internal collections.
4.  Ownership should influence object creation.
5.  Prefer composition for HAS-A relationships.
6.  Use inheritance only for genuine IS-A/substitutability
    relationships.
7.  Entities have stable identity.
8.  Value Objects are defined by their values.
9.  `equals()` and `hashCode()` should follow business semantics.
10. Use Tell, Don't Ask.
11. Keep commands and queries conceptually separate.
12. Constructors should establish valid initial state.
13. Use YAGNI; do not generalize prematurely.
14. Public APIs should expose business capabilities rather than
    implementation details.
15. A program can work correctly while still having poor domain design.
16. Business invariants should be protected by the domain model.
17. Composition and ownership are related but do not require
    nested/private classes.
18. Use strong types and abstractions only when they solve a real
    problem.
19. Avoid God objects.
20. Avoid pattern-driven design.

------------------------------------------------------------------------

# 16. Sprint 1 Test Coverage

We successfully exercised:

``` text
✓ Create Restaurant
✓ Create Menu
✓ Create FoodItems
✓ Create MenuItems
✓ Add MenuItems to Menu
✓ Query available items
✓ Query vegetarian items
✓ Customer owns Cart
✓ Add first CartItem
✓ Add same item again
✓ Increase quantity
✓ Add another item from same Restaurant
✓ Calculate Cart total
✓ Decrease quantity
✓ Remove CartItem
✓ Clear final CartItem
✓ Reset Restaurant when Cart becomes empty
✓ Reject item from another Restaurant
✓ Re-add after Cart becomes empty
✓ Protect CartItems with unmodifiable collection
```

The Cart integration completed successfully with exit code 0.

------------------------------------------------------------------------

# 17. Sprint 1 Final Assessment

The domain model is **solid for the current learning stage**.

The most valuable achievement was not the number of classes.

It was learning to ask:

``` text
Who owns this?
Who creates this?
Who changes this?
Who protects this rule?
Should this be public?
Should this object know about that object?
Is this identity or value?
Is this HAS-A or IS-A?
```

That is the foundation for the next phase.

------------------------------------------------------------------------

# Sprint 2 Preview

Sprint 2 will introduce the next major business flow:

``` text
Cart
  ↓
Checkout
  ↓
Order
  ↓
OrderItem
```

The complexity increases because we now have to deal with **state
transitions, snapshots, and business consistency**.

We will focus on:

1.  Order as an Entity
2.  OrderItem as an Entity/value-like component
3.  Checkout responsibilities
4.  Order lifecycle/state machine
5.  Pricing snapshots
6.  Cart → Order conversion
7.  Preventing mutation of completed Orders
8.  Payment concept
9.  Delivery/address snapshot
10. Order history
11. Exception/business-rule design
12. Testing state transitions

------------------------------------------------------------------------

# Sprint 2 Core Questions

We will learn to reason about questions such as:

> What happens to the Cart after checkout?

> Should OrderItem reference MenuItem or copy its data?

> What if the restaurant changes the price after checkout?

> What if the customer's address changes after placing an Order?

> Can a CANCELLED Order become CONFIRMED?

> Who is allowed to transition Order state?

> Should Payment belong to Order?

> Is Checkout an Entity, Service, or Use Case?

> What happens if payment succeeds but Order creation fails?

These questions will push us from basic OOP into real LLD thinking.

------------------------------------------------------------------------

# Sprint 2 Deliverables

By the end of Sprint 2 we should have:

``` text
Customer
Restaurant
Menu
FoodItem
MenuItem
Cart
CartItem
Order
OrderItem
Payment
AddressSnapshot
```

plus the relevant state/enum models and tests.

We will continue the same workflow:

``` text
Discuss
   ↓
Design
   ↓
You reason
   ↓
Pair program
   ↓
Run
   ↓
Test business scenarios
   ↓
Senior Engineer PR Review
   ↓
🔴 🟡 🟢 classification
   ↓
EOD .md ceremony
```

------------------------------------------------------------------------

# Important Sprint 2 Rule

We will NOT blindly carry Sprint 1 design into Sprint 2.

Every relationship gets reconsidered.

For example:

``` text
CartItem → MenuItem
```

may be perfectly reasonable.

But:

``` text
OrderItem → MenuItem
```

may create a historical consistency problem.

That distinction is exactly the kind of reasoning expected in LLD
interviews.

------------------------------------------------------------------------

# Sprint 2 Success Criteria

By the end of Sprint 2, you should be able to explain:

> "Why is Order different from Cart?"

> "Why does Order need a snapshot?"

> "Why shouldn't completed Orders depend on mutable MenuItems?"

> "Who owns OrderItems?"

> "Who controls Order state transitions?"

> "Why can't an Order be arbitrarily modified?"

If you can answer those naturally, Sprint 2 has succeeded.

------------------------------------------------------------------------

# 18. EOD Documentation Strategy From Sprint 2

We will stop creating random `.md` files.

Instead, we'll maintain a small documentation system.

``` text
docs/
│
├── 00-roadmap.md
├── 01-engineering-principles.md
│
├── sprint-01/
│   ├── sprint-01-wrap.md
│   ├── architecture-decisions.md
│   └── senior-pr-review.md
│
├── sprint-02/
│   ├── sprint-02-wrap.md
│   ├── architecture-decisions.md
│   └── senior-pr-review.md
│
└── interview/
    ├── lld-patterns.md
    ├── java-concepts.md
    ├── design-questions.md
    └── mistakes-and-lessons.md
```

The important change is that **we separate learning notes from
architecture decisions and PR reviews**.

At the end of each sprint:

### `sprint-X-wrap.md`

Contains:

-   What we built
-   What we learned
-   Important code/design decisions
-   Business rules
-   Tests
-   🔴🟡🟢 review
-   Interview takeaways

### `architecture-decisions.md`

Only significant decisions.

For example:

``` text
ADR-001: Customer owns Cart
ADR-002: FoodItem separated from MenuItem
ADR-003: Cart supports one Restaurant
ADR-004: Menu owns MenuItems
ADR-005: CartItem owns quantity
```

Each decision will have:

``` text
Context
Decision
Alternatives considered
Why we chose it
Trade-offs
```

### `senior-pr-review.md`

This is our **engineering growth log**:

``` text
Issue
Why it matters
How we detected it
Correct design
Interview lesson
```

This will become extremely valuable when revising before interviews.

------------------------------------------------------------------------

# Final Sprint 1 Statement

> **Sprint 1 established the foundation of our domain-driven object
> model. We learned to design around ownership, identity, encapsulation,
> invariants, composition, and business responsibilities rather than
> simply creating data-holder classes.**

Sprint 1 is officially **CLOSED**.

Sprint 2 starts with:

``` text
                    CART
                     │
                     ▼
                  CHECKOUT
                     │
                     ▼
                   ORDER
                     │
              ┌──────┴──────┐
              ▼             ▼
          ORDER ITEMS     PAYMENT
```

And this is where the LLD difficulty starts going up. 🚀
