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