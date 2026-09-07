# Sprint 3 — Day 2: REST APIs, Controllers & Spring MVC

## 1. Day 2 focus

Day 1 covered Spring Boot fundamentals:

- ApplicationContext
- Beans
- IoC
- Dependency Injection
- Component scanning
- `@Service`
- `@Repository`
- Constructor injection
- Bean scopes and lifecycle

Day 2 moved into the HTTP/API layer.

Core architecture:

```text
Client
  |
  | HTTP Request
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
Database
```

---

## 2. REST APIs

REST APIs expose resources through HTTP.

Swiggy resources include:

- Restaurant
- Customer
- Cart
- Order
- Delivery
- Payment

Typical REST-style APIs:

```http
GET    /restaurants/101
POST   /orders
GET    /orders/5001
PATCH  /orders/5001
DELETE /orders/5001
```

Common HTTP methods:

| Method | Typical purpose |
|---|---|
| GET | Read |
| POST | Create |
| PUT | Replace/update |
| PATCH | Partial update |
| DELETE | Delete |

---

## 3. `@RestController`

```java
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
}
```

`@RestController` tells Spring that the class handles HTTP requests and method return values are normally written directly to the HTTP response.

Conceptually:

```text
@Controller
+
@ResponseBody
=
@RestController
```

---

## 4. `@RequestMapping`

```java
@RequestMapping("/restaurants")
```

defines the common URL prefix for the controller.

Therefore:

```java
@GetMapping
```

maps to:

```text
GET /restaurants
```

and:

```java
@GetMapping("/popular")
```

would map to:

```text
GET /restaurants/popular
```

---

## 5. `@GetMapping`

Example:

```java
@GetMapping("/{restaurantId}")
public Restaurant getRestaurant(
        @PathVariable int restaurantId) {

    return restaurantService.getRestaurant(restaurantId);
}
```

This matches:

```http
GET /restaurants/101
```

---

## 6. Constructor Injection

Controller:

```java
private final RestaurantService restaurantService;

public RestaurantController(
        RestaurantService restaurantService) {

    this.restaurantService = restaurantService;
}
```

The constructor is ordinary Java.

### Plain Java

We could manually wire the objects in `main()`:

```java
RestaurantService restaurantService =
        new RestaurantService();

RestaurantController controller =
        new RestaurantController(restaurantService);
```

### Spring

Spring creates the beans and supplies the service:

```text
Spring Container
      |
      +---- creates RestaurantService
      |
      +---- creates RestaurantController
                    |
                    | injects
                    v
             RestaurantService
```

This is constructor-based Dependency Injection.

Benefits:

- Required dependencies are explicit.
- Dependencies can be `final`.
- The object cannot be constructed without required dependencies.
- Easy unit testing.
- Dependencies are not hidden.

---

## 7. `@PathVariable`

For:

```java
@GetMapping("/{restaurantId}")
public Restaurant getRestaurant(
        @PathVariable int restaurantId) {
    ...
}
```

the request:

```http
GET /restaurants/101
```

matches:

```text
/restaurants/{restaurantId}
```

`{restaurantId}` is a placeholder.

Spring extracts:

```text
101
```

and supplies it to:

```java
int restaurantId
```

Conceptually:

```text
GET /restaurants/101
             |
             v
       restaurantId = 101
             |
             v
       getRestaurant(101)
```

Explicit form:

```java
@PathVariable("restaurantId") int restaurantId
```

---

## 8. Path Variable vs Query Parameter

### Path variable

```http
GET /restaurants/101
```

```java
@PathVariable int restaurantId
```

Usually identifies a specific resource.

Think:

> Which resource?

Examples:

```text
/restaurants/101
/orders/5001
/customers/200
```

### Query parameter

```http
GET /restaurants?lat=17.49&lng=78.39
```

```java
@RequestParam double lat
@RequestParam double lng
```

Usually provides filtering/searching/options.

Think:

> How should I retrieve/filter the collection?

---

## 9. `@RequestBody`

For a POST request:

```http
POST /restaurants
Content-Type: application/json

{
    "name": "Paradise"
}
```

Controller:

```java
@PostMapping
public String createRestaurant(
        @RequestBody CreateRestaurantRequest request) {

    return "Creating restaurant: " + request.getName();
}
```

The flow:

```text
JSON
  |
  v
Jackson
  |
  v
CreateRestaurantRequest
  |
  v
Controller
```

JSON → Java is **deserialization**.

Java → JSON is **serialization**.

Spring/Jackson handles this conversion.

---

## 10. Request DTO

Example:

```java
public class CreateRestaurantRequest {

    private String name;

    public CreateRestaurantRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

Client sends:

```json
{
    "name": "Paradise"
}
```

Jackson conceptually creates/populates the Java object:

```java
CreateRestaurantRequest request =
        new CreateRestaurantRequest();

request.setName("Paradise");
```

The application does not manually parse the JSON.

---

## 11. Controller vs Service

Avoid putting business logic directly into controllers.

Bad:

```text
Controller
  |
  +-- database queries
  +-- business rules
  +-- calculations
  +-- state changes
  +-- etc.
```

Better:

```text
HTTP
  |
  v
Controller
  |
  v
Service
  |
  v
Repository
  |
  v
Database
```

Controller:

> Handles the HTTP/API boundary.

Service:

> Coordinates the application/use-case logic.

Repository:

> Handles persistence.

Domain:

> Owns business state and behavior.

---

## 12. Spring MVC Request Lifecycle

For:

```http
GET /restaurants/101
```

high-level flow:

```text
HTTP Request
     |
     v
DispatcherServlet
     |
     v
HandlerMapping
     |
     v
RestaurantController
     |
     | @PathVariable
     v
restaurantId = 101
     |
     v
RestaurantService
     |
     v
Repository
     |
     v
Database
```

On the way back:

```text
Database
   |
   v
Repository
   |
   v
Service
   |
   v
Controller
   |
   v
Java Response
   |
   v
Jackson
   |
   v
JSON
   |
   v
HTTP Response
```

### `DispatcherServlet`

Central entry point for Spring MVC HTTP requests. It coordinates request processing.

### HandlerMapping

Spring maintains mappings for controller methods.

For:

```java
@RequestMapping("/restaurants")
@GetMapping("/{restaurantId}")
```

Spring has a mapping corresponding to:

```text
GET /restaurants/{restaurantId}
```

When `/restaurants/101` arrives, Spring identifies the matching controller method.

---

## 13. Domain Models vs Spring Beans

Not every class should be a Spring bean.

Our domain classes:

```text
Restaurant
Address
Menu
MenuItem
Customer
Cart
Order
OrderItem
```

can remain ordinary Java objects.

Spring-managed application components:

```text
RestaurantController
RestaurantService
CheckoutService
Repositories
PaymentService
...
```

Conceptually:

```text
SPRING CONTAINER
────────────────────────
RestaurantController
RestaurantService
CheckoutService
Repository
...


DOMAIN OBJECTS
────────────────────────
Restaurant #101
Restaurant #102
Customer #501
Order #5001
Address
Menu
...
```

Do not automatically add `@Component` to domain classes.

---

## 14. `Address` as a Value Object

Our existing `Address` is:

```java
public final class Address {

    private final String houseNumber;
    private final String street;
    private final String area;
    private final String city;
    private final String state;
    private final String postalCode;

    ...
}
```

It is an immutable Value Object.

Characteristics:

- `final` class
- `final` fields
- validation
- `equals()`
- `hashCode()`
- `toString()`

It is not a Spring bean.

We construct it as an ordinary Java object:

```java
Address address = new Address(
        "4-1-123",
        "Abids Road",
        "Abids",
        "Hyderabad",
        "Telangana",
        "500001"
);
```

A `Restaurant` can contain this `Address`:

```java
Restaurant restaurant =
        new Restaurant(
                101,
                "Paradise",
                address
        );
```

Spring manages the service/controller around these domain objects; it does not need to manage every `Address` instance.

A small correction to `getFullAddress()` was discussed: include `area`, since `area` is part of the address.

```java
public String getFullAddress() {
    return houseNumber + ", " +
            street + ", " +
            area + ", " +
            city + ", " +
            state + " - " +
            postalCode;
}
```

---

## 15. Temporary In-Memory RestaurantService

Because database/JPA has not been introduced yet, restaurant data can temporarily live in memory.

Conceptually:

```java
@Service
public class RestaurantService {

    private final List<Restaurant> restaurants =
            new ArrayList<>();

    public List<Restaurant> getRestaurants() {
        return List.copyOf(restaurants);
    }

    public Restaurant getRestaurant(int restaurantId) {

        return restaurants.stream()
                .filter(restaurant ->
                        restaurant.getId() == restaurantId)
                .findFirst()
                .orElse(null);
    }
}
```

This is intentionally temporary.

Eventually:

```text
RestaurantController
        |
        v
RestaurantService
        |
        v
RestaurantRepository
        |
        v
PostgreSQL
```

The service should not permanently own restaurant persistence data.

---

## 16. Why not expose domain objects directly?

We temporarily returned:

```java
List<Restaurant>
```

from the controller to focus on REST mechanics.

But in a production-style architecture, directly exposing the domain model couples the API to internal implementation.

For example, `Restaurant` contains:

```text
id
name
address
menu
rating
open
internal fields...
```

The API may not want to expose all of these.

This leads to DTOs and API boundaries.

Desired direction:

### Request

```text
JSON
 ↓
CreateRestaurantRequest
 ↓
Controller
 ↓
Service
 ↓
Restaurant
```

### Response

```text
Restaurant
 ↓
RestaurantResponse
 ↓
Controller
 ↓
JSON
```

DTOs separate the external API contract from the internal domain model.

---

# 17. Day 2 Architecture

```text
                         CLIENT
                           |
                           | HTTP
                           v
                +----------------------+
                |  RestaurantController|
                +----------+-----------+
                           |
                           v
                +----------------------+
                |   RestaurantService  |
                +----------+-----------+
                           |
                           v
                +----------------------+
                | RestaurantRepository |
                +----------+-----------+
                           |
                           v
                       DATABASE
```

Domain:

```text
Restaurant
    |
    +── Address
    |
    +── Menu
          |
          +── MenuItem
```

Spring manages the application components around the domain.

---

# 18. Important mental model

Spring Boot does not replace normal Java.

It provides infrastructure around our Java application:

```text
Plain Java domain
        +
Spring IoC / DI
        +
Spring MVC
        +
Persistence
        +
Security
        +
Other infrastructure
```

The architecture is gradually becoming:

```text
Java / LLD
     |
     v
Domain Models
     |
     v
Spring IoC / DI
     |
     v
REST Controllers
     |
     v
HTTP ↔ Java
     |
     v
Java ↔ JSON
     |
     v
DTO/API boundaries
```

---

# 19. Next: Sprint 3 Day 4

Next session starts with:

```text
DTOs
  ↓
Request/Response API boundaries
  ↓
Mapping DTO ↔ Domain
  ↓
Validation
  ↓
Exception handling
  ↓
Clean Restaurant APIs
```

Then we will apply the same architecture to our existing `Order` domain.

The goal is to connect the LLD work already done with production-style Spring Boot architecture.
