# Architecture Decision Records (ADR)

---

## ADR-001

Decision

Restaurant owns Menu.

Reason

A Menu cannot exist independently.

---

## ADR-002

Decision

Menu owns FoodItems.

Reason

FoodItems belong to a specific Menu.

---

## ADR-003

Decision

Address is a Value Object.

Reason

Identity is determined by values.

---

## ADR-004

Decision

Address is immutable.

Reason

Changing an Address changes its business meaning.

---

## ADR-005

Decision

Customer owns Addresses.

Reason

Customer manages the address lifecycle.

---

## ADR-006

Decision

Address does not know Customer.

Reason

Avoid bidirectional coupling.

---

## ADR-007

Decision

Default Address is managed by Customer.

Reason

It is a business responsibility.

---

## ADR-008

Decision

House Number and Street are optional.

Reason

Not all real-world addresses contain them.

---

## ADR-009

Decision

Optional text fields are normalized to empty strings.

Reason

Avoid repetitive null handling.

---

## ADR-010

Decision

Validation methods remain private.

Reason

Validation is an implementation detail.

---

## ADR-011

Decision

Objects validate themselves.

Reason

Objects should always be born valid.

## ADR-012

Decision

Customer owns List<Address> directly.

Reason

Address management is currently simple.

Introducing AddressBook would be premature.

---

## ADR-013

Decision

Customer creates its own Address collection.

Reason

Ownership includes creation.

---

## ADR-014

Decision

Menu owns FoodItems.

Reason

Menu owns the collection.

Restaurant should not expose addFoodItem().

---

## ADR-015

Decision

Customer owns Address operations.

Reason

Customer owns the collection.

Address remains a passive Value Object.

---

## ADR-016

Decision

Address remains immutable.

Reason

Historical correctness.

Previous Orders should never change.

---

## ADR-017

Decision

Menu remains mutable.

Reason

Business naturally expects menus
to evolve over time.

---

## ADR-018

Decision

Customer equality is based on customerId.

Reason

Customer is an Entity.

Address equality remains value-based.