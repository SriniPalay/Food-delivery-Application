# HTTP, Connections, Sessions & Authentication — Today's Notes

## Study Context

These notes capture what we discussed today using a **Swiggy + Cart** example.

The main goal was to build the correct mental model for:

1. HTTP request/response
2. Request latency
3. TCP connections
4. Persistent HTTP connections
5. Application login/session
6. Authentication tokens
7. Server-side sessions vs token-based authentication
8. How all of these fit together in a backend system

---

# 1. HTTP Request–Response Cycle

When a client (for example, the Swiggy mobile app) communicates with a backend, it generally follows:

```text
Client
  |
  | HTTP Request
  v
Server
  |
  | Processing
  | Database / Other Services
  v
HTTP Response
  |
  v
Client
```

An HTTP request could look conceptually like:

```http
GET /restaurants/123
```

The server processes the request and returns something like:

```http
HTTP/1.1 200 OK
Content-Type: application/json

{
    "restaurantId": 123,
    "name": "Paradise"
}
```

The important point:

> HTTP is a protocol used to communicate requests and responses. It does not have one fixed response time.

---

# 2. Is an HTTP Request–Response Cycle 330 ms?

No.

There is **no fixed HTTP latency** such as 330 ms.

A request could take:

```text
10 ms
50 ms
100 ms
330 ms
500 ms
2 seconds
```

depending on the system and network.

A simplified latency breakdown might be:

```text
DNS                         20 ms
TCP + TLS                   50 ms
Network                     40 ms
Server processing           80 ms
Database                    100 ms
Response/network            40 ms
--------------------------------
Total                      330 ms
```

This is only an example.

The better mental model is:

> Total request latency = network + connection setup + server processing + database/other service calls + response transfer.

Do NOT memorize:

> "HTTP takes 330 ms."

Instead remember:

> "This particular API request took 330 ms."

---

# 3. Swiggy Example — Opening a Restaurant

Suppose you open a restaurant page.

The app might make a request similar to:

```http
GET /restaurants/123
```

A simplified backend flow:

```text
Your Phone
    |
    | GET /restaurants/123
    v
API Gateway
    |
    v
Restaurant Service
    |
    v
Database
```

The database returns restaurant information.

The service creates a response:

```json
{
    "restaurantId": 123,
    "name": "Paradise",
    "items": [
        {
            "id": 501,
            "name": "Chicken Biryani",
            "price": 299
        }
    ]
}
```

That response goes back:

```text
Database
   |
Restaurant Service
   |
API Gateway
   |
Internet
   |
Your Phone
```

---

# 4. Swiggy Example — Add an Item to Cart

Suppose you tap:

```text
Chicken Biryani → Add
```

The app might send:

```http
POST /cart/items
```

with data similar to:

```json
{
    "restaurantId": 123,
    "itemId": 501,
    "quantity": 1
}
```

The high-level flow is:

```text
Your Phone
    |
    | POST /cart/items
    v
Internet
    |
    v
API Gateway
    |
    v
Cart Service
    |
    +----> Authentication
    |
    +----> Redis
    |
    +----> Restaurant Service
    |
    +----> Database
    |
    v
Updated Cart
    |
    v
API Gateway
    |
    v
Your Phone
```

---

# 5. What Can the Cart Service Do?

The Cart Service may need to determine:

> "Who is making this request?"

The request may contain an authentication token:

```http
Authorization: Bearer <token>
```

The backend validates the token and identifies the user:

```text
userId = 987
```

Then it retrieves the user's cart.

For example:

```text
Cart 987

Restaurant: 123

Chicken Biryani × 1
Coke × 1
```

---

# 6. Restaurant Validation

A food-delivery cart often has business rules.

For example:

```text
Existing cart restaurant = 100
New item restaurant      = 123
```

Because:

```text
100 != 123
```

the Cart Service may reject the operation and tell the client that the cart contains items from another restaurant.

If the restaurant matches:

```text
Existing restaurant = 123
New item restaurant = 123
```

the item can be added.

The cart might become:

```text
Chicken Biryani × 2
Coke × 1
```

The updated cart can then be persisted in a database and/or cached in Redis depending on the architecture.

---

# 7. Request vs Response

The most important basic flow is:

```text
REQUEST

Client
  |
  | HTTP Request
  v
Server
  |
  | Business Logic
  | DB / Other Services
  v


RESPONSE

Server
  |
  | HTTP Response
  v
Client
```

The request asks:

> "I want something done."

The response tells the client:

> "Here is the result."

---

# 8. TCP Connection vs HTTP

This is where things become more interesting.

HTTP operates above the underlying transport/network layers.

A traditional mental model is:

```text
HTTP
 ↓
TCP
 ↓
IP
 ↓
Network
```

So:

- **TCP** provides the underlying reliable connection.
- **HTTP** defines how requests and responses are communicated.

With modern HTTP/3, the transport is different:

```text
HTTP/3
  ↓
QUIC
  ↓
UDP
  ↓
IP
```

For now, the most important concept is simply:

> HTTP and TCP are not the same thing.

---

# 9. Does Every HTTP Request Create a New Connection?

Not necessarily.

Older/simple behavior could look like:

```text
Request 1
   ↓
TCP connection
   ↓
Response 1
   ↓
Connection closes

Request 2
   ↓
NEW TCP connection
   ↓
Response 2
```

That is inefficient.

Modern HTTP commonly supports **persistent connections**, where a connection can be reused:

```text
TCP Connection
────────────────────────────

Request 1 → Response 1

Request 2 → Response 2

Request 3 → Response 3

Request 4 → Response 4
```

The connection may eventually become idle and be closed.

---

# 10. How Long Does a Connection Stay Alive?

There is no universal duration.

The connection can remain open while it is considered useful, and it can eventually be closed because of:

- idle timeout
- server policy
- client policy
- proxy/load-balancer policy
- network issues
- connection errors
- resource management

So do NOT memorize:

> "HTTP connections stay alive for exactly X seconds."

Instead remember:

> A persistent connection can be reused, but it is not guaranteed to remain open forever.

---

# 11. What Happens If the Connection Closes?

Suppose:

```text
10:00 → Swiggy request
10:01 → Response
10:05 → Connection has become idle and was closed
10:10 → You tap Add to Cart
```

The app can establish/use another connection.

Conceptually:

```text
NEW CONNECTION
      |
      v
HTTP Request
      |
      v
Cart Service
      |
      v
HTTP Response
```

Your login does NOT automatically disappear just because the TCP connection disappeared.

This leads to the most important distinction of today's lesson.

---

# 12. TCP Connection ≠ Login

Think of these as separate concepts.

## TCP Connection

It answers:

> "Can these two machines currently communicate over this connection?"

It can:

```text
OPEN
 ↓
USED
 ↓
IDLE
 ↓
CLOSED
```

## Application Login

It answers:

> "Who is this user?"

For example:

```text
User ID = 987
```

The application needs some mechanism to recognize User 987 across requests.

That mechanism can involve:

- server-side sessions
- cookies/session IDs
- access tokens
- JWTs
- other authentication mechanisms

---

# 13. Hotel Analogy

A useful analogy:

### TCP Connection = Phone Call

```text
You ───────── Phone Call ───────── Hotel
```

You can hang up.

That does not mean your hotel reservation disappears.

### HTTP Request = Question

You ask:

> "Is room 305 available?"

The hotel answers.

### Login/Session = Reservation

Your reservation/account remains even after the phone call ends.

Similarly:

```text
TCP connection closes
        ↓
Login/session can still be valid
```

This is a very important mental model.

---

# 14. How Does Swiggy Know Who You Are?

After authentication, the client needs to provide proof of identity to later requests.

One common model uses an access token.

Conceptually:

```text
Login
  |
  v
Server verifies user
  |
  v
Access Token
  |
  v
Mobile App stores token securely
```

Later:

```http
GET /cart

Authorization: Bearer <access-token>
```

The backend validates the token and determines:

```text
Token
  ↓
User 987
```

Then:

```text
User 987
  ↓
Cart Service
  ↓
Fetch cart
  ↓
Return cart
```

---

# 15. Important: Login Is Not the TCP Connection

Consider this timeline:

```text
10:00
Login request
    ↓
Login successful
    ↓
Authentication state established

10:01
TCP connection closes

10:10
New TCP connection
    ↓
GET /cart
    ↓
Authorization token
    ↓
Server identifies User 987
    ↓
Cart returned
```

The user can still be logged in even though the earlier TCP connection no longer exists.

Therefore:

> Network connection lifetime and application authentication lifetime are different things.

---

# 16. Server-Side Session Authentication

One traditional approach is a server-side session.

After login:

```text
User
  |
  | Login
  v
Server
  |
  | Creates session
  v
Session Store

Session ID = XYZ123
User ID    = 987
```

The client receives a session identifier.

Later:

```text
GET /cart
Session ID: XYZ123
```

The server looks it up:

```text
XYZ123
  ↓
User 987
```

Then it can fetch:

```text
User 987's cart
```

The important idea:

> The server stores the session information.

---

# 17. Token-Based Authentication

Another approach is token-based authentication.

The client sends:

```http
Authorization: Bearer <token>
```

The server validates the token.

If the token is valid:

```text
Token
  ↓
Valid
  ↓
User 987
```

Then the request continues.

A common example is JWT.

A JWT can contain claims such as:

```text
userId
expiry
issuer
etc.
```

The exact contents and architecture depend on the system.

---

# 18. Server-Side Session vs Token-Based Authentication

### Server-side session

```text
Client
  |
  | Session ID
  v
Server
  |
  | Look up session
  v
Session Store
```

The server/session store maintains the state.

### Token-based

```text
Client
  |
  | Token
  v
Server
  |
  | Validate token
  v
Authenticated User
```

The token itself carries information/claims that can help establish identity, depending on the token format and implementation.

---

# 19. The Three Layers to Keep Separate

This is the most important summary.

```text
┌─────────────────────────────────────┐
│ APPLICATION                         │
│                                     │
│ Login / Authentication / Session   │
│ "I am User 987"                     │
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│ HTTP                                │
│                                     │
│ GET /cart                           │
│ POST /cart/items                    │
│ Requests / Responses                │
└──────────────────┬──────────────────┘
                   │
┌──────────────────▼──────────────────┐
│ TRANSPORT / NETWORK                 │
│                                     │
│ TCP connection (HTTP/1.1, HTTP/2)  │
│ or QUIC (HTTP/3)                   │
└─────────────────────────────────────┘
```

### In simple language

| Concept | Main question |
|---|---|
| TCP connection | Can these machines communicate over this connection? |
| HTTP request | What does the client want right now? |
| HTTP response | What result is the server returning? |
| Authentication | Who is this user? |
| Session/token | How does the system recognize/verify the user across requests? |

---

# 20. Microservices Make This More Interesting

A large system may have many backend services:

```text
                    API Gateway
                         |
          ┌──────────────┼──────────────┐
          ↓              ↓              ↓
    Cart Service   Restaurant      User Service
                     Service
          |
          ↓
        Redis
          |
          ↓
       Database
```

A Cart Service might communicate with other services:

```text
Cart Service
     |
     ├────> Restaurant Service
     |
     ├────> Pricing Service
     |
     ├────> Inventory Service
     |
     └────> Database/Cache
```

Each service-to-service call can introduce additional network latency.

This is one reason system design cares about:

- latency
- number of network calls
- caching
- connection reuse
- service boundaries
- load balancing
- database performance

---

# 21. Load Balancer Question

Imagine Swiggy has many Cart Service instances:

```text
                  Load Balancer
                 /      |      \
                ↓       ↓       ↓
             Server A Server B Server C
```

You might send:

```text
Request 1 → Server A
Request 2 → Server B
Request 3 → Server C
```

Now the important question is:

> How does Server B know that Request 2 belongs to the same authenticated user that Server A saw in Request 1?

This leads directly into the next concepts:

- stateless authentication
- JWT
- session stores
- Redis
- sticky sessions
- load balancing
- distributed sessions

---

# 22. Today's Complete Mental Model

When a user taps "Add to Cart":

```text
                    USER
                     |
                     | 1. Tap Add
                     v
              Mobile Application
                     |
                     | 2. HTTP Request
                     |    + Authentication
                     v
                  Network
                     |
                     v
                API Gateway
                     |
                     v
                Cart Service
                     |
          ┌──────────┼───────────┐
          ↓          ↓           ↓
     Authentication Redis     Database
          |
          ↓
       User 987
                     |
                     v
              Updated Cart
                     |
                     v
               HTTP Response
                     |
                     v
               Mobile App
```

Underneath the HTTP communication there is a network transport mechanism.

The application-level login/session is a separate concept from the lifetime of that network connection.

---

# 23. What You Should Remember From Today

If you remember only these points, today's study was successful:

### Point 1

**HTTP does not have a fixed 330 ms latency.**

330 ms is just an example of an API's total request latency.

### Point 2

**HTTP and TCP are different things.**

```text
HTTP
 ↓
TCP (for HTTP/1.1 and HTTP/2)
```

HTTP/3 uses QUIC instead.

### Point 3

**A TCP connection can be reused.**

You don't necessarily create a brand-new TCP connection for every HTTP request.

### Point 4

**A TCP connection can close without logging the user out.**

```text
TCP connection = communication channel

Login/session = application state/identity
```

### Point 5

**Authentication identifies the user.**

For example:

```text
User 987
```

### Point 6

**Session/token mechanisms allow subsequent requests to be associated with the authenticated user.**

### Point 7

**In distributed systems, authentication becomes more interesting when multiple servers are involved.**

That is the bridge to tomorrow's topic.

---

# Tomorrow's Goal

## Topic: Stateless Authentication, JWT, Sessions, Redis & Load Balancing

We should continue from today's final question:

> "If Request 1 goes to Server A and Request 2 goes to Server B, how does Server B know that the request belongs to the same user?"

Tomorrow's learning path:

```text
Today's Concepts
      |
      v
Authentication
      |
      v
Session vs Token
      |
      v
JWT
      |
      v
Stateless Authentication
      |
      v
Redis
      |
      v
Load Balancer
      |
      v
Multiple Backend Servers
      |
      v
How a large system maintains user identity
```

### Tomorrow's practical example

We'll continue using:

```text
Swiggy
   ↓
Login
   ↓
Add to Cart
   ↓
Request → Server A
   ↓
Request → Server B
   ↓
How does Server B identify User 987?
```

Then we'll build the architecture step by step.

---

# Final Mental Model

```text
                    APPLICATION
               ┌──────────────────┐
               │ Login            │
               │ Authentication   │
               │ Session / JWT    │
               └────────┬─────────┘
                        │
                        ▼
                      HTTP
               ┌──────────────────┐
               │ Request          │
               │ Response         │
               └────────┬─────────┘
                        │
                        ▼
                 NETWORK LAYER
               ┌──────────────────┐
               │ TCP / QUIC       │
               │ Connections      │
               └──────────────────┘
```

**Connection:** "Can I communicate?"

**HTTP:** "What do I want?"

**Authentication:** "Who am I?"

**Session/Token:** "How do you recognize/verify me across requests?"

**Tomorrow:** "How does this work when there are hundreds or thousands of backend servers?"
