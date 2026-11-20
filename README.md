# LRU Cache

A Java implementation of a **Least Recently Used (LRU) cache** built from scratch with a `HashMap` for O(1) key lookup and a hand-written doubly linked list for O(1) recency tracking.

When the cache is full, inserting a new key evicts the entry that was used least recently.

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [How It Works](#how-it-works)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Demo Walkthrough](#demo-walkthrough)
- [Complexity](#complexity)
- [Known Limitations](#known-limitations)
- [Future Scope](#future-scope)

## Features

- `put(key, value)` inserts a new entry or updates an existing one, and marks it as most recently used.
- `get(key)` returns the value for a key (or `null` if absent) and marks it as most recently used.
- Automatic eviction of the least recently used entry once capacity is reached.
- No external dependencies; uses only the Java standard library.

## Project Structure

```
LRU Cache/
├── src/
│   ├── LRUCache.java        # Public cache API: get/put and eviction logic
│   ├── DoubleLinkList.java  # Doubly linked list with sentinel head/tail nodes
│   ├── Node.java            # List/map entry: key, value, prev and next pointers
│   └── Client.java          # Demo program showing eviction behaviour
├── out/                     # Compiled output (generated, git-ignored)
├── .idea/                   # IntelliJ IDEA project settings
├── LRUCache.iml             # IntelliJ module file
├── .gitignore
└── README.md
```

## How It Works

The cache combines two data structures:

| Structure | Role |
|-----------|------|
| `HashMap<String, Node>` | Maps each key to its node for constant-time lookup. |
| `DoubleLinkList` | Orders nodes by recency. The node after `head` is the most recently used, and the node before `tail` is the least recently used. |

Operations:

- **`get(key)`**: if the key exists, its node is moved to the front of the list and its value is returned. Otherwise `null` is returned.
- **`put(key, value)`**:
  - If the key exists, its value is replaced and the node is moved to the front.
  - If the key is new and the cache is full, the node at the back of the list (the LRU entry) is removed from both the list and the map. The new node is then added to the front.
- **`DoubleLinkList`** uses two sentinel nodes (`head` and `tail`) so that insertions and removals never need null checks at the ends of the list.

## Requirements

- JDK 8 or later (the code uses the diamond operator `<>`, which requires Java 7+; JDK 17 is the configured language level in the IntelliJ project)
- Optional: IntelliJ IDEA, for opening the project as-is

## Getting Started

### Option 1: IntelliJ IDEA

1. Open the `LRU Cache` folder as a project.
2. Mark `src/` as a Sources Root if it is not already.
3. Run `Client`.

### Option 2: Command line

From the project root:

```bash
# Compile into the out/ directory
javac -d out src/*.java

# Run the demo
java -cp out Client
```

On Windows PowerShell, the same commands work as written.

## Usage

The classes are package-private, so client code must live in the same package (the default package in this project). A minimal example:

```java
LRUCache cache = new LRUCache(2);   // capacity of 2

cache.put("a", 1);
cache.put("b", 2);

Integer v = cache.get("a");         // returns 1; "a" becomes most recently used
cache.put("c", 3);                  // evicts "b", the least recently used entry

System.out.println(cache.get("b")); // null: evicted
System.out.println(cache.get("c")); // 3
```

### API

| Method | Returns | Description |
|--------|---------|-------------|
| `LRUCache(int capacity)` | — | Creates an empty cache holding up to `capacity` entries. |
| `Integer get(String key)` | value, or `null` | Looks up a key and marks it as most recently used. |
| `void put(String key, int value)` | — | Inserts or updates a key. Evicts the LRU entry if the cache is full. |

## Demo Walkthrough

`Client.java` runs a scripted scenario with a capacity of 3:

1. Insert `a`, `b`, `c`. Recency order is `b` (LRU) → `c` → `a` (MRU).
2. `get("a")` moves `a` to the front. Order becomes `b` → `c` → `a`.
3. `put("d", 4)` evicts `b`. `get("b")` returns `null`.
4. `c`, `a` and `d` are still present.
5. `put("c", 30)` updates the value and makes `c` the most recently used.
6. `put("e", 5)` evicts `a`, which is now the LRU entry.

Expected output ends with `=== Demo Complete ===`.

> Note: the "Order now" lines in `Client.java` are printed text, not values read from the cache. The behaviour they describe is verified by the `get` results printed alongside them.

## Complexity

| Operation | Time | Space |
|-----------|------|-------|
| `get` | O(1) average | — |
| `put` | O(1) average | — |
| Overall | — | O(capacity) |

The O(1) bounds depend on `HashMap` hashing being O(1) on average.

## Known Limitations

- **Not thread-safe.** Concurrent calls to `get` or `put` can corrupt the map and list. Wrap the cache with external synchronization if it is shared between threads.
- **Capacity of 0 fails.** `new LRUCache(0)` followed by `put` throws a `NullPointerException`, because eviction runs on an empty list and `removeLast()` returns `null`.
- **Negative capacity is accepted** without validation. Eviction only triggers when the size equals the capacity, so with a negative value it never triggers and the cache grows without bound.
- **Fixed types.** Keys are always `String` and values are always `int`/`Integer`. There is no generic support.
- **No `remove` or `containsKey` method** on the public API, even though `DoubleLinkList` has a `remove(Node)` helper.
- **Package-private classes.** `LRUCache`, `DoubleLinkList` and `Node` cannot be used from another package without changing their access modifiers.
- **No tests.** The only verification is the manual demo in `Client.java`.

## Future Scope

Planned or possible improvements, roughly in order of value:

### Correctness and robustness
- Validate the constructor argument and reject `capacity <= 0` with an `IllegalArgumentException`.
- Null-check keys and values in `put` and `get`.
- Fix the empty-list eviction path so it does not throw when capacity is 0.

### API
- Make the cache generic: `LRUCache<K, V>`.
- Add `remove(K key)`, `containsKey(K key)`, `size()`, `clear()` and `isEmpty()`.
- Return `Optional<V>` from `get` instead of a nullable `Integer`, or keep `null` for simplicity and document it.
- Add an `Iterable` or snapshot view of entries in recency order, for debugging.
- Accept an eviction callback, so callers can react when an entry is evicted.

### Concurrency
- Provide a thread-safe variant, either by synchronizing the public methods or by using a `ReentrantLock`.
- Consider a `ConcurrentHashMap`-backed design with striped locks for higher throughput.

### Features
- Time-to-live (TTL) expiry per entry, alongside the LRU policy.
- Hit and miss counters, with a `stats()` method.
- Optional `LinkedHashMap` (`accessOrder = true`) implementation, to compare against the hand-written version.
- Bulk operations such as `putAll` and `getAll`.

### Testing and tooling
- Add JUnit 5 unit tests covering eviction order, updates, capacity 1 and capacity 0.
- Add a benchmark (JMH) comparing this implementation with `LinkedHashMap`.
- Move the build to Maven or Gradle so the project can be built and tested without IntelliJ.
- Add a CI workflow that builds and runs the tests on every push.

### Documentation
- Add Javadoc to all public methods.
- Add a diagram of the list and map state before and after an eviction.
