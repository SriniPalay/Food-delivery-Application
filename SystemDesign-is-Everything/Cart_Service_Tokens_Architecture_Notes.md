# Microservices Architecture: Bearer Tokens vs. Session Tokens in Cart Services

---

## 1. Executive Summary

In modern, high-throughput distributed systems (such as **Swiggy**, **Zomato**, or **Uber Eats**), handling identity and transient transaction state requires decoupling **Authentication (AuthN) / Authorization (AuthZ)** from **Cart & Session State Management**.

A common design pattern involves sending **both** an **OAuth Bearer Token** and a **Session / Cart Identifier** simultaneously in API request headers.

---

## 2. Token Comparison: Bearer Token vs. Session / Cart Token

| Feature / Dimension | Bearer Token (Identity & Auth) | Session / Cart ID Token (State & Context) |
| :--- | :--- | :--- |
| **Primary Responsibility** | Answers: *"Who is the user, and what are their permissions?"* | Answers: *"Which active workspace / checkout context is being mutated?"* |
| **Common Format** | Cryptographically signed JSON Web Token (JWT) or OAuth Access Token. | Opaque string / Cryptographically secure random UUID v4. |
| **Location in Request** | `Authorization: Bearer <JWT>` header | `X-Session-ID`, `X-Cart-ID`, or `HttpOnly` Cookie |
| **Storage & Persistence** | Secure Client Storage (Keychain, Encrypted SharedPreferences), Auth Cache. | In-Memory Distributed Cache (**Redis Cluster**, Aerospike, DynamoDB). |
| **State Nature** | **Stateless** or loosely coupled with token revocation lists. | **Stateful** (Contains item IDs, quantities, active restaurant locks, coupons). |
| **Guest Support** | Null or anonymous scope prior to authentication. | Fully operational for guest browsing and pre-login cart construction. |

---

## 3. Why Send Both Headers Simultaneously?

Even when a user is **already logged in**, client applications (iOS, Android, Web) pass both tokens to the Cart Service.

```http
POST /api/v1/cart/items HTTP/1.1
Host: api.swiggy.com
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
X-Session-ID: sess_f47ac10b-58cc-4372-a567-0e02b2c3d479
X-Cart-ID: cart_98214_active
Content-Type: application/json

{
  "restaurant_id": "rest_442",
  "item_id": "item_8871",
  "quantity": 2
}
```

### Architectural Drivers:

1. **Multi-Device / Multi-Tab Concurrency Isolation**
   * A single user account (`userId: 98214`) can be active on multiple devices (e.g., iPhone app and Web checkout).
   * If the Cart Service relied exclusively on `userId` extracted from the Bearer Token, actions taken on one device would overwrite or corrupt in-flight checkouts on another.
   * The `Session-ID` / `Cart-ID` isolates the local execution branch.

2. **Single-Restaurant Locking & Cart Constraints**
   * Food delivery platforms strictly enforce single-restaurant cart rules.
   * The session context maintains transient locks, pricing freezes, surge fee locks, and applied promotion codes across microservice boundaries.

3. **High-Speed Cache Partitioning (Redis Key Lookup)**
   * Cart operations (add, remove, quantity update) are write-heavy and demand sub-5ms latencies.
   * The Cart Service skips heavy relational database queries and performs atomic in-memory mutations directly on Redis keys:
     ```text
     Redis Key: cart:f47ac10b-58cc-4372-a567-0e02b2c3d479
     Value: {
       "user_id": 98214,
       "restaurant_id": "rest_442",
       "items": [{"item_id": "item_8871", "quantity": 2}],
       "locked": false
     }
     TTL: 86400 seconds (24h)
     ```

4. **API Gateway Offloading**
   * The **API Gateway / Edge Proxy** validates cryptographic signatures, checks revocation lists, and extracts user identity from the Bearer token.
   * It enriches downstream microservice calls with internal headers (`X-User-ID: 98214`) while passing through the `X-Session-ID` to the Cart Service.

5. **Guest Cart Hydration (Pre-to-Post Login Transition)**
   * An unauthenticated user adds items using an allocated guest UUID session.
   * Upon logging in, the client issues the request with the newly issued Bearer token alongside the existing `Session-ID`.
   * The Cart Service securely links or merges the guest cart bucket to the authenticated user account without state loss.

---

## 4. Session Token Structure: The Role of UUID

The session identifier does not need to encode payload data. In modern architectures, it is typically an **Opaque UUID v4**:

* **Stateless Pointer:** Contains no embedded claims or business data.
* **Security via Unpredictability:** Generated using cryptographically strong pseudo-random number generators ($2^{122}$ possible combinations), preventing enumeration or guessing attacks.
* **Direct Cache Indexing:** Functions as the exact hash key for fast distributed in-memory lookups.

---

## 5. Session / Cart ID Lifecycle & Termination Triggers

A session ID / Cart UUID is invalidated, rotated, or evicted under the following event-driven and temporal conditions:

```
[ User Action / Event ]
         │
         ├─── Order Placed (Success) ──────> State exported to Order DB; Redis Cart deleted
         │
         ├─── Switch Restaurant ───────────> Discard old cart state; Clear/Reset Cart UUID
         │
         ├─── Inactivity Timeout (TTL) ────> Key evicted automatically by Redis
         │
         ├─── Explicit User Logout ────────> Session unlinked and cleared on client & server
         │
         └─── Empty / Clear Cart ──────────> Cart container purged or reset
```

### Lifecycle Transition Matrix

| Trigger Event | Mechanism | Backend Action | Token Outcome |
| :--- | :--- | :--- | :--- |
| **Checkout / Order Placement** | Payment confirmed -> Order Service webhook | Cart state converted to permanent `Order` entity; Redis cart cache deleted. | **Terminated & Replaced** with fresh session on next view. |
| **Inactivity / Abandonment** | Time-To-Live (TTL) expiration | Redis automatically evicts the key after inactivity window (e.g., 24h–72h sliding TTL). | **Expired / Evicted** |
| **Restaurant Switch** | User confirms "Replace Cart" dialog | Cart Service flushes items belonging to the previous restaurant. | **Cleared / Reset** |
| **User Logout** | User logs out from app/web | API Gateway / Auth service broadcasts revocation; Client destroys local session pointers. | **Revoked & Discarded** |
| **Manual Item Clear** | User removes final item | Cart container is emptied or marked inactive. | **Emptied / Reset** |

---

## 6. Summary Architectural Architecture Diagram

```
+-----------------------------------------------------------------------------------+
|                                  CLIENT (App / Web)                               |
| Sends: Authorization: Bearer <JWT>                                                |
|        X-Session-ID: sess_uuid_v4                                                 |
+----------------------------------------+------------------------------------------+
                                         |
                                         v
                         +-------------------------------+
                         |          API GATEWAY          |
                         | - Validates Bearer JWT        |
                         | - Verifies Expiry & Signature |
                         | - Injects X-User-ID: 98214    |
                         +---------------+---------------+
                                         |
                                         v
                         +-------------------------------+
                         |        CART MICROSERVICE      |
                         | - Identifies Context via UUID |
                         | - Applies Business Rules      |
                         +---------------+---------------+
                                         |
                                         v
                         +-------------------------------+
                         |     DISTRIBUTED CACHE (Redis) |
                         | Key: cart:sess_uuid_v4        |
                         | Value: Items, Locks, Surges   |
                         +-------------------------------+
```
