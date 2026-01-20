This project focuses on handling concurrency in an order processing system.
To achieve this, I have used Akka Typed, which is based on the Actor Model.

Akka helps in managing concurrent operations by processing one message at a time per actor, which eliminates race conditions and avoids the need for locks or shared mutable state.

In this system, the code is organized in a modular way by separating:
 -Business logic
 -Actor logic
 -Data access logic
 
 The system is built using three main actors:

OrderManager:
Acts as the entry point for order creation. It is responsible for creating a new OrderActor for each incoming order.

OrderActor:
Follows the actor‑per‑order pattern. Each order is processed by its own actor, allowing multiple orders to be handled concurrently and independently.

InventoryActor:
Manages inventory operations. It checks product availability and reserves stock in a thread‑safe manner.

When a client places an order, the OrderManager creates a new OrderActor. The OrderActor then communicates with the InventoryActor to determine whether the order can be fulfilled. Based on the inventory response, the order is either confirmed or rejected due to insufficient stock. The final order status is sent back to the client, and once processing is complete, the OrderActor stops itself.

