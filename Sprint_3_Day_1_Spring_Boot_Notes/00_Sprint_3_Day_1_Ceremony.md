# Sprint 3 — Day 1 Ceremony

## Theme
Spring Boot foundations: Spring Container, IoC, Beans, Component Scanning, and Dependency Injection.

## What we completed
- Converted the existing Maven project to Spring Boot.
- Aligned Maven configuration to Java 25.
- Added `spring-boot-starter-web`.
- Added the Spring Boot Maven plugin.
- Build succeeded.
- Created `SwiggyApplication` with `@SpringBootApplication`.
- Successfully started the Spring Boot application.
- Obtained the `ApplicationContext`.
- Converted `CheckoutService` to a `@Service`.
- Converted `InMemoryOrderRepository` to a `@Repository`.
- Verified that Spring creates both Beans.
- Verified constructor dependency injection.

## Key result

Previously:

```java
OrderRepository repository =
        new InMemoryOrderRepository();

CheckoutService service =
        new CheckoutService(repository);
```

Now Spring performs the object creation and wiring.

## Verified output

```text
Checkout Service Bean:
org.swiggy.services.CheckoutService@...

Checkout Service Bean:
org.swiggy.repositories.InMemoryOrderRepository@...
```

## Day 1 mental model

```text
Spring Application
       |
       v
ApplicationContext
       |
       +---- CheckoutService Bean
       |            |
       |            | needs
       |            v
       |     OrderRepository
       |            ^
       |            |
       +---- InMemoryOrderRepository Bean
```

## Not covered yet
REST, DTOs, JPA, PostgreSQL, Redis, Kafka, Security, deployment.

## Takeaway
> Spring manages objects and their relationships. Our plain-Java DI design made the transition to Spring natural.
