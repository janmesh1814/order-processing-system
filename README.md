## 📦 Overview

This project focuses on handling **concurrency** in an **order processing system**.  
To achieve this, **Akka Typed** is used, which is based on the **Actor Model**.

Akka helps manage concurrent operations by ensuring that each actor processes **one message at a time**. This approach eliminates race conditions and avoids the need for locks or shared mutable state.

---

## 🧩 Design Approach

The codebase is organized in a **modular and clean architecture**, with clear separation of responsibilities:

- **Business logic**
- **Actor logic**
- **Data access logic**

This separation makes the system easier to understand, test, and extend.

---

## 🏗️ Core Actors

The system is built using three main actors:

### 🔹 OrderManager
- Acts as the **entry point** for order creation
- Creates a new `OrderActor` for each incoming order
- Coordinates the order processing workflow

### 🔹 OrderActor
- Implements the **actor‑per‑order** pattern
- Each order is handled by its **own dedicated actor**
- Enables multiple orders to be processed **concurrently and independently**

### 🔹 InventoryActor
- Manages inventory‑related operations
- Checks product availability
- Reserves stock in a **thread‑safe** manner

---

## 🔄 Order Processing Flow

1. A client places an order.
2. `OrderManager` receives the request and creates a new `OrderActor`.
3. `OrderActor` communicates with `InventoryActor` to verify stock availability.
4. Based on the inventory response:
   - ✅ The order is **confirmed**, or  
   - ❌ The order is **rejected** due to insufficient stock.
5. The final order status is sent back to the client.
6. Once processing is complete, the `OrderActor` **stops itself**.

---

## ✅ Key Benefits

- Safe concurrent order processing
- No locks or shared mutable state
- Clear separation of concerns
- Scalable and fault‑isolated design
