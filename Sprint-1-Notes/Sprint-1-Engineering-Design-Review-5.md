# Sprint 1 - Day 5
# Backend Engineering Learning Notes

> Project: Food Delivery Backend (Swiggy Clone)
>
> Goal:
> Learn Java by designing and building a production-quality backend from scratch.
> Every Java concept must naturally arise from a business requirement.

---

# What We Designed Today

Today's session focused on designing the Address and Customer domain model.

Unlike previous sessions, today was heavily focused on engineering decisions rather than implementation.

Topics Covered

- Entity vs Value Object
- Ownership
- Immutability
- Business Rules
- Object Responsibilities
- Constructor Validation
- equals()
- hashCode()
- Object Consistency
- Domain Invariants
- Product Thinking

---

# Address is a Value Object

A Value Object has no identity.

Two Address objects are equal if all of their values are equal.

Example

Address A

Flat 302
Green Valley
Hyderabad

Address B

Flat 302
Green Valley
Hyderabad

Business considers them identical.

Therefore Address should override

- equals()
- hashCode()

---

# Why Address is Immutable

We discussed a real business scenario.

Customer moves from Hyderabad to Bangalore.

Should we update the existing Address?

No.

Because previous orders would now incorrectly show Bangalore.

Instead

Old Address remains unchanged.

New Address is created.

This preserves historical correctness.

---

# Ownership

Customer owns Address.

Address does NOT know Customer.

Reason:

The owner should manage the lifecycle.

Customer

- creates Address
- removes Address
- changes default Address

Address only represents a location.

---

# Responsibilities

Customer

Responsible for

- Managing addresses
- Default address
- Business rules

Address

Responsible for

- Representing a delivery location
- Validating itself
- Comparing equality

Nothing more.

---

# Optional vs Required Fields

Optional

- House Number
- Street

Mandatory

- Area
- City
- State
- Postal Code

Reason:

Real-world addresses may not always contain house numbers or street names.

---

# Normalization

Optional text fields are normalized.

Example

Input

null

Stored

""

Reason

Avoid unnecessary NullPointerExceptions.

Simplifies String operations.

---

# Constructor Validation

Objects should always be born valid.

Constructor validates mandatory fields.

Invalid Address objects should never exist.

---

# Why Validation is Private

Validation is an implementation detail.

Other objects should only know whether an Address can be created.

They should not know how validation works.

---

# equals()

equals()

determines

Business Equality

Not

Reference Equality.

Java

==

compares references.

equals()

compares values.

---

# hashCode()

hashCode()

does NOT replace equals().

Instead

It improves lookup performance.

Hash-based collections

- HashMap
- HashSet

first calculate hashCode()

then call equals()

only if necessary.

---

# hashCode Contract

If

equals()

returns true

hashCode()

must return the same value.

The opposite is NOT required.

Different objects may share the same hashCode.

This is called a Hash Collision.

---

# Biggest Learning

Never memorize

equals()

or

hashCode()

Understand

why

they exist.

IDE can generate syntax.

Only engineers understand the design.