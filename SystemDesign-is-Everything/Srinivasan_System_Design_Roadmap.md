# System Design Mastery Roadmap
### Goal: Backend Developer role, ₹15+ LPA, within 4 months
### Format: 4 Levels → 4 Sprints (2 weeks each) → 8 weeks total → 2 hrs/day, 6 days/week + 1 revision day/week

---

## How to use this plan

- **6 days a week**: new topic, 2 focused hours (1 hr theory/reading, 1 hr applying it — draw it, code it, or explain it out loud).
- **1 day a week (Sunday)**: full revision — no new material. Re-explain every topic from that week out loud, from memory, like you're in the interview. If you stumble, that's your real study list for next week.
- **Every topic ends with**: "Could I explain this to an interviewer in 90 seconds, with a trade-off?" If no, flag it and revisit in the sprint's revision day.
- **Every design problem uses this framework** (write it on a sticky note): Clarify requirements → Estimate scale (back-of-envelope math) → High-level design → Deep dive on 1–2 components → Trade-offs & failure modes.
- Draw everything. Use Excalidraw or draw.io. Diagramming is ~50% of how you're evaluated — a correct answer with no diagram reads worse than an imperfect one with a clear diagram.
- Where it overlaps with your actual work (Spring Boot POC, Expense Splitter, Parking Lot LLD), use your real project as the practice ground instead of a generic example. It sticks better and gives you a real story for behavioral rounds too.

---

## LEVEL 1 — FOUNDATIONS
### Sprint 1, Weeks 1–2

**Why first:** Every HLD/LLD answer you'll ever give rests on this. Skipping it is the #1 reason candidates freeze mid-interview.

#### Week 1 — Core Distributed Systems Building Blocks
| Day | Topic | What "done" looks like |
|---|---|---|
| 1 | Client-server model, DNS, TCP/IP basics, HTTP/HTTPS lifecycle | Can trace a request from browser → server → response, naming every hop |
| 2 | Vertical vs horizontal scaling, stateless vs stateful services | Can explain why stateless services scale horizontally more easily |
| 3 | Latency vs throughput, Load Balancers (round robin, least connections, consistent hashing intro) | Can pick a LB algorithm for a given scenario and justify it |
| 4 | Caching fundamentals — cache-aside, write-through, write-back, LRU/LFU eviction | Can draw a cache-aside flow and explain a cache invalidation bug |
| 5 | CAP theorem + PACELC | Can classify 3 real systems (e.g. DynamoDB, MySQL, Cassandra) by CAP trade-off |
| 6 | Forward proxy vs reverse proxy, CDN basics | Can explain why a CDN reduces latency and when it doesn't help |
| 7 | **REVISION** — re-derive all 6 topics out loud, no notes | You can teach Day 1–6 to someone else in 45 min total |

#### Week 2 — OOP, SOLID & Design Patterns (LLD Foundations)
| Day | Topic | What "done" looks like |
|---|---|---|
| 8 | OOP pillars + SOLID: Single Responsibility, Open/Closed | Can spot an SRP violation in your own POC code |
| 9 | SOLID: Liskov Substitution, Interface Segregation, Dependency Inversion | Can refactor a bad example live |
| 10 | UML basics — class diagrams, sequence diagrams | Can hand-draw a class diagram for a 3-class system in under 5 min |
| 11 | Creational patterns — Singleton, Factory, Builder | Can identify where each fits in a Spring Boot app (e.g. `@Bean` = Factory-ish) |
| 12 | Structural patterns — Adapter, Decorator, Facade | Can give a real-world Java example for each |
| 13 | Behavioral patterns — Strategy, Observer, State | Map Strategy back to your Expense Splitter split-type design |
| 14 | **REVISION** — Sprint 1 full recap + self-quiz (write 10 questions, answer cold) | You pass your own quiz without peeking |

**Sprint 1 checkpoint:** You should be able to whiteboard "how does a web request scale" and "design a Notification class with Strategy pattern" without hesitation.

---

## LEVEL 2 — CORE HLD BUILDING BLOCKS
### Sprint 2, Weeks 3–4

**Why second:** This is the vocabulary every HLD interview is built from — databases, caching, and communication. This is also where your resume gap (Hibernate/JPA territory) gets reinforced conceptually.

#### Week 3 — Data Layer Deep Dive
| Day | Topic | What "done" looks like |
|---|---|---|
| 15 | SQL deep dive — indexing (B-tree), normalization, ACID transactions | Can explain why an index speeds up a query and its write-cost trade-off |
| 16 | NoSQL types — key-value, document, column-family, graph — when to use each | Can justify DB choice for 3 different scenarios (session store, product catalog, social graph) |
| 17 | Replication — leader-follower, multi-leader, failover | Can explain replication lag and its user-facing impact |
| 18 | Sharding/partitioning — range-based, hash-based, consistent hashing (deep, not intro) | Can design a shard key for a given entity and explain hot-shard risk |
| 19 | Caching strategies deep dive — Redis patterns, distributed cache invalidation | Can explain thundering herd and a fix (locking, jitter) |
| 20 | CDN & edge caching deep dive | Can explain cache-control headers and static vs dynamic content caching |
| 21 | **REVISION** | Teach Week 3 out loud in 45 min |

#### Week 4 — Communication & Messaging
| Day | Topic | What "done" looks like |
|---|---|---|
| 22 | API design — REST best practices, pagination, versioning, idempotency | Can design a paginated, idempotent REST API for a real resource |
| 23 | Message queues — Kafka vs RabbitMQ, pub-sub vs point-to-point | Can explain when you'd pick Kafka over RabbitMQ (ties to your Kafka exposure at work) |
| 24 | Async processing & event-driven architecture | Can design an order-processing flow using events |
| 25 | Rate limiting algorithms — token bucket, leaky bucket, sliding window | Can implement token bucket in Java from memory |
| 26 | Consistency models — strong vs eventual, quorum reads/writes | Can explain eventual consistency with a concrete user-facing example |
| 27 | Microservices communication — sync vs async, service discovery, API gateway | Can draw a 4-service architecture with a gateway and explain failure isolation |
| 28 | **REVISION** — Sprint 2 full recap + self-quiz | Pass your own quiz cold |

**Sprint 2 checkpoint:** You should be able to answer "how would you scale a read-heavy API to 1M users" citing caching, replication, and CDN together, with trade-offs.

---

## LEVEL 3 — DISTRIBUTED SYSTEMS PATTERNS & LLD MASTERY
### Sprint 3, Weeks 5–6

**Why third:** This is what separates "I know the vocabulary" from "I can be trusted with production systems" — the exact bar 2026 interviewers are raising. It's also where you go deep on LLD, correcting the self-review gap you already identified in your Parking Lot work.

#### Week 5 — Distributed Systems Patterns
| Day | Topic | What "done" looks like |
|---|---|---|
| 29 | Consensus (Paxos/Raft, conceptual level) + leader election | Can explain why distributed systems need consensus in plain English |
| 30 | Distributed transactions — 2-Phase Commit, Saga pattern | Can explain Saga with a concrete e-commerce order example |
| 31 | Fault tolerance — circuit breaker, retries with backoff, bulkhead, timeouts | Can explain what happens without a circuit breaker (cascading failure) |
| 32 | Observability — logging, metrics, distributed tracing, health checks | Can name what you'd monitor for a payment service and why |
| 33 | Security in system design — authN/authZ, OAuth2, JWT, abuse-prevention rate limits | Can explain JWT vs session-based auth trade-offs |
| 34 | Capacity estimation — back-of-envelope math practice (QPS, storage, bandwidth) | Can estimate storage for "1M daily active users, X writes/day" in under 5 min |
| 35 | **REVISION** | Teach Week 5 out loud in 45 min |

#### Week 6 — LLD Problem Practice (with self-review discipline)
| Day | Topic | What "done" looks like |
|---|---|---|
| 36 | Revisit Parking Lot LLD — this time, self-review BEFORE showing anyone: check SOLID violations, missing edge cases | Written self-review checklist completed before "submission" |
| 37 | LLD — Elevator System | Full class diagram + working Java code |
| 38 | LLD — Library Management System | Full class diagram + working Java code |
| 39 | LLD — Rate Limiter (code it, not just describe it) | Working token-bucket implementation in Java |
| 40 | LLD — Tic-Tac-Toe or Chess (state machine design) | Clean state representation, extensible for new rules |
| 41 | LLD — Expense Splitter, round 2 — add a new split type live, time yourself | Adding a new Strategy takes under 15 min, no core code touched |
| 42 | **REVISION** — Sprint 3 recap + redo self-review checklist on Day 36–41 work | Every LLD solution has a written self-review note attached |

**Sprint 3 checkpoint:** You should be able to take any LLD prompt cold, produce a class diagram in 10 minutes, working code in 30, and catch your own SOLID violations before anyone else does.

---

## LEVEL 4 — FULL MOCK SYSTEMS & INTERVIEW SIMULATION
### Sprint 4, Weeks 7–8

**Why last:** Everything above is ingredients. This is where you cook full meals under time pressure — the actual shape of the interview.

#### Week 7 — Classic HLD Problems (end-to-end, timed)
| Day | Topic | What "done" looks like |
|---|---|---|
| 43 | Design a URL Shortener | Full framework applied, 45 min timed |
| 44 | Design Pastebin / a distributed Rate Limiter service | Full framework applied, 45 min timed |
| 45 | Design Twitter/X news feed | Fan-out on write vs read discussed explicitly |
| 46 | Design WhatsApp/a chat system | Message delivery guarantees (at-least-once) discussed |
| 47 | Design Uber/ride-hailing | Geospatial indexing (quad-tree/geohash) discussed |
| 48 | Design Netflix/YouTube (video streaming + CDN) | Chunked upload, transcoding pipeline, CDN tie-in discussed |
| 49 | **REVISION** | Redo one Day 43–48 problem from scratch, no notes, under 40 min |

#### Week 8 — Advanced Systems + Mock Interviews
| Day | Topic | What "done" looks like |
|---|---|---|
| 50 | Design Instagram (feed + notifications) | Full framework applied, 45 min timed |
| 51 | Design a distributed Job Scheduler / Notification system | Full framework applied, 45 min timed |
| 52 | AI-system-design literacy — RAG pipelines, vector DBs, LLM serving basics (batching, KV-cache) at a conceptual level | Can hold a 5-min conversation on this even if it's not your specialty — it's showing up in 2026 loops even outside AI-first companies |
| 53 | **Mock interview #1** — have a friend/mentor give you a random problem, record yourself | Self-review the recording against the framework checklist |
| 54 | **Mock interview #2** — different problem, ideally different person | Compare against Mock #1 — is your structure improving? |
| 55 | Targeted review of weak areas surfaced by the two mocks | Every weak area gets one focused re-study block |
| 56 | **FINAL REVISION** — flashcard blitz across all 4 levels, explain 5 random topics cold | You can go 8 weeks back and still explain Day 1's content clearly |

**Sprint 4 checkpoint / exit bar:** You can take any unseen HLD prompt, structure it correctly in the first 2 minutes, and defend at least one trade-off decision under pushback — without notes.

---

## Resources (used to build this plan, worth having open)
- *System Design Interview* Vol. 1 & 2 — Alex Xu (ByteByteGo) — best single reference for HLD problems in Weeks 7–8
- *Designing Data-Intensive Applications* — Martin Kleppmann — the deepest read, best for Sprint 2–3 material
- Hello Interview "System Design in a Hurry" — good for rapid review before an actual interview
- ByteByteGo / Gaurav Sen YouTube — visual explanations, good for Sprint 1 if a topic isn't clicking from text

## Notes specific to your situation
- Since your day job is a Java/Spring Boot POC, code every LLD problem in Java — it reinforces both tracks at once and gives you real "I built this" stories.
- Your Expense Splitter already demonstrates Strategy pattern — use it as your go-to example in Week 2 and Week 6 rather than inventing a new one from scratch.
- Given the resume conversation we just had: once you've actually built something with Spring Data JPA (not just read about it), that closes a real gap — worth slotting a small hands-on repository exercise into a POC sprint at work if you can.
- Track your Sunday revision misses in a running list — that list becomes your Week 8 "weak areas" target list, so start it from Day 7 onward.
