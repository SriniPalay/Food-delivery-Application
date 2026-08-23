# Spring Boot Core — Day 1

## 1. Why Spring?

Plain Java required manual object wiring:

```java
OrderRepository repository =
        new InMemoryOrderRepository();

CheckoutService service =
        new CheckoutService(repository);
```

Spring takes over much of this object-management responsibility.

## 2. IoC — Inversion of Control

Without Spring:

```text
Our code
  |
  +-- creates objects
  +-- connects dependencies
  +-- controls lifecycle
```

With Spring:

```text
Spring Container
  |
  +-- creates objects
  +-- connects dependencies
  +-- manages lifecycle
```

IoC is the broader idea. Dependency Injection is a major technique used to achieve it.

## 3. Spring Bean

A Spring Bean is an object whose creation and lifecycle are managed by the Spring container.

Important:
> Bean does not mean "something another object needs."

A Bean can be a dependency of another Bean.

Our Beans:
- `CheckoutService`
- `InMemoryOrderRepository`

Our domain classes such as `Customer`, `Order`, `Cart`, and `Address` do not automatically become Beans just because they exist.

## 4. ApplicationContext

`ApplicationContext` is the Spring container we interacted with.

```java
ApplicationContext context =
        SpringApplication.run(
                SwiggyApplication.class,
                args
        );
```

For learning we can retrieve a Bean:

```java
CheckoutService service =
        context.getBean(CheckoutService.class);
```

In normal application code, prefer injection rather than repeatedly calling `getBean()`.

## 5. Component Scanning

Spring Boot scans the application's component packages and discovers classes marked as Spring components.

```text
org.swiggy
|
+-- SwiggyApplication
+-- services
|    +-- CheckoutService
+-- repositories
     +-- InMemoryOrderRepository
```

With `@Service` and `@Repository`, Spring registers those classes as Beans.

## 6. `@Service`

```java
@Service
public class CheckoutService {
}
```

Tells Spring to manage the class as a Bean and communicates that it is a service-layer component.

## 7. `@Repository`

```java
@Repository
public class InMemoryOrderRepository
        implements OrderRepository {
}
```

Tells Spring to manage it as a Bean and communicates that it belongs to the repository/persistence boundary.

## 8. Constructor Injection

Our existing constructor:

```java
public CheckoutService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
}
```

lets Spring see:

```text
CheckoutService
      |
      | requires
      v
OrderRepository
      ^
      |
implements
      |
InMemoryOrderRepository
```

Spring finds a suitable Bean and injects it.

## 9. DIP vs DI

### Dependency Inversion
Question:
> What should CheckoutService depend on?

Answer:
```java
OrderRepository
```
rather than:
```java
InMemoryOrderRepository
```

### Dependency Injection
Question:
> How does CheckoutService receive the dependency?

Answer:
Through its constructor.

Memory rule:

```text
DIP = what we depend on
DI  = how the dependency is supplied
```

## 10. `@Bean` is not old

`@Bean` is a current Spring mechanism.

Component scanning:

```java
@Service
public class CheckoutService {}
```

Explicit configuration:

```java
@Configuration
public class SwiggyConfig {

    @Bean
    public OrderRepository orderRepository() {
        return new InMemoryOrderRepository();
    }
}
```

Use `@Bean` when explicit construction/configuration is useful, including third-party classes that we do not own.

## 11. Interface-based dependency resolution

We can ask:

```java
context.getBean(OrderRepository.class);
```

and Spring can return `InMemoryOrderRepository` because it implements `OrderRepository`.

This fits our Repository + DIP design.

## 12. Spring Singleton Scope vs Singleton Pattern

Spring Beans have singleton scope by default: within a particular ApplicationContext, Spring normally maintains one instance of a singleton-scoped Bean.

This is NOT the classic Singleton design pattern.

Classic Singleton requires application-level implementation such as a static instance and `getInstance()`.

We did not implement that pattern.

## 13. The core transformation

Yesterday:

```java
OrderRepository repository =
        new InMemoryOrderRepository();

CheckoutService service =
        new CheckoutService(repository);
```

Today:

```text
Spring Container
      |
      +-- creates Repository Bean
      +-- creates Service Bean
      +-- injects Repository
```

Our architecture did not fundamentally change. Spring took over the wiring mechanism.

## Interview answers

**What is IoC?**
> Control over object creation, wiring, and lifecycle is transferred from application code to a container/framework.

**What is a Bean?**
> An object whose creation and lifecycle are managed by the Spring container.

**What is ApplicationContext?**
> The Spring container that manages and provides Beans.

**What is component scanning?**
> Spring discovers component classes such as `@Service` and `@Repository` and registers them as Beans.

**Why constructor injection?**
> It makes dependencies explicit, supports immutable fields, and lets Spring provide required dependencies.

**Is `@Bean` old?**
> No. It is a current mechanism for explicitly registering an object as a Bean.

**Is Spring singleton scope the Singleton design pattern?**
> No. Spring singleton is a container-managed Bean scope; the classic Singleton is an application-level design implementation.
