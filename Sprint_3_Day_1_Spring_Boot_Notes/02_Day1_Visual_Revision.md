# Sprint 3 Day 1 — Visual Revision

## Plain Java vs Spring

```text
PLAIN JAVA

Main
 |
 +-- new InMemoryOrderRepository()
 |
 +-- new CheckoutService(repository)
 |
 +-- manually connect objects
```

```text
SPRING

SwiggyApplication
 |
 +-- SpringApplication.run()
          |
          v
   ApplicationContext
          |
          +-- creates Repository Bean
          +-- creates Service Bean
          +-- injects Repository
          +-- manages lifecycle
```

## Bean relationship

```text
             SPRING CONTAINER
    +------------------------------+
    |                              |
    | InMemoryOrderRepository      |
    |          BEAN                |
    |              |               |
    |              | implements    |
    |              v               |
    |       OrderRepository        |
    |              |               |
    |              | injected      |
    |              v               |
    |       CheckoutService        |
    |            BEAN              |
    |                              |
    +------------------------------+
```

## Dependency Inversion

```text
BAD
CheckoutService
      |
      v
InMemoryOrderRepository
```

```text
BETTER
CheckoutService
      |
      v
OrderRepository
      ^
      |
InMemoryOrderRepository
```

## Dependency Injection

```text
OrderRepository Bean
        |
        | supplied by Spring
        v
CheckoutService constructor

public CheckoutService(
        OrderRepository repository) {
    this.repository = repository;
}
```

## Component scanning

```text
@SpringBootApplication
        |
        v
Component Scan
        |
        +-- @Service --> CheckoutService Bean
        |
        +-- @Repository --> InMemoryOrderRepository Bean
```

## Annotation mental model

```text
@Service
   |
   +-- application/service component

@Repository
   |
   +-- repository/persistence component

@Bean
   |
   +-- explicitly create/register an object
```

## Spring singleton scope

```text
ApplicationContext
       |
       +-------------------+
       |                   |
       v                   v
   Service A           Service B
       |                   |
       +---------+---------+
                 |
                 v
       same singleton-scoped
          Repository Bean
```

This is not the same as implementing the classic Singleton pattern.
