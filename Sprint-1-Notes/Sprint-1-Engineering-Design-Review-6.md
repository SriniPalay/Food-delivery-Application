# Sprint 1 - Day 6
# Customer Entity & Engineering Design

---

# Goal

Design and implement the Customer entity.

Unlike previous days, today's focus shifted from learning Java syntax
to applying engineering principles while building a production-quality
domain model.

Discussion : 20%

Implementation : 80%

---

# Customer is an Entity

Customer has identity.

Identity never changes.

Therefore

private final int customerId;

Unlike Address,

Customer equality is determined by identity,
not by all field values.

---

# Responsibilities of Customer

Customer is responsible for

- Managing profile information
- Managing delivery addresses

Customer is NOT responsible for

- Placing Orders
- Processing Payments
- Restaurant Operations
- Menu Management
- Delivery Partner

---

# Customer Fields

private final int customerId;

private String name;

private String email;

private String phoneNumber;

private final List<Address> addresses;

private Address defaultAddress;

Reason

Customer owns the address collection.

Therefore Customer manages it.

---

# Constructor Design

Customer creates its own Address collection.

Reason

Ownership includes creation.

No external object should provide Customer's internal collection.

Constructor flow

Validate

↓

Assign

↓

Create Address List

---

# Validation Strategy

Instead of writing one giant validation method,
we separated validation into two layers.

Layer 1

Generic Validation

validateRequiredText()

Responsibilities

- Null Check
- Trim
- Blank Check

Layer 2

Business Validation

validateName()

validateEmail()

validatePhoneNumber()

validateCustomerId()

Each method applies its own business rules.

---

# Validation Rules

Customer ID

- Greater than zero

Name

- Required
- Minimum 2 characters

Email

- Required
- Contains '@'
- Contains '.'

Phone Number

- Required
- Exactly 10 digits

---

# addAddress()

Business Rules

- Address cannot be null.
- Duplicate addresses are not allowed.
- First address automatically becomes default.
- Customer owns Address collection.

Used

addresses.contains(address)

instead of manually iterating.

Reason

Expresses intent better.

---

# removeAddress()

Business Rules

- Address cannot be null.
- Address must exist.
- Default address cannot be removed.
- User must first choose another default.

Design Change

Originally

Customer must always have one address.

After discussion

Customer may have zero addresses.

Reason

Having an address is required only during Order Placement,
not during Customer Registration.

Business rules belong where they matter.

---

# changeDefaultAddress()

Business Rules

- Address cannot be null.
- Address must belong to the customer.
- Setting the same address again is allowed.
- No exception required.

This is an idempotent operation.

---

# Why Address is Immutable

Address represents historical information.

Changing

Hyderabad

↓

Bangalore

would incorrectly modify previous orders.

Correct Solution

Create a new Address.

Old Address remains unchanged.

---

# Why Menu is Mutable

Business expects Menu to change.

Restaurant owners

- Add food
- Remove food
- Update menu

Therefore Menu naturally exposes

addFoodItem()

removeFoodItem()

---

# final Class vs Immutable Object

These are NOT the same.

final class

↓

Prevents inheritance.

Immutable Object

↓

Prevents state changes.

Address became immutable because

- final fields
- no setters
- no modifying methods
- constructor initialization

Making Address final only protects the design
from being broken by subclasses.

---

# Why Customer Has addAddress()

Customer owns

List<Address>

Therefore

Customer manages it.

---

# Why Menu Has addFoodItem()

Menu owns

List<FoodItem>

Therefore

Menu manages it.

Restaurant should not expose

addFoodItem()

because Restaurant does not own the collection.

It owns a Menu.

---

# Why No AddressBook?

Question discussed

Should Customer own

AddressBook

↓

Addresses

instead of

List<Address> ?

Decision

No.

Reason

Address management currently has only

- Add
- Remove
- Change Default

Creating another abstraction would be unnecessary.

If Address management becomes more complex later,
AddressBook can be introduced.

---

# Customer Equality

Address

↓

equals()

↓

Compares values.

Customer

↓

equals()

↓

Compares customerId only.

Reason

Customer is an Entity.

Address is a Value Object.

---

# Today's Biggest Learning

Java did not decide which classes are mutable.

The Business Domain did.

Business first.

Java second.