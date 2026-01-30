package com.example.orders

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import com.example.orders.actors._
import com.example.orders.actors.OrderActor.OrderResult
import com.example.orders.repository.InventoryRepository
import com.example.orders.config.DatabaseInitializer

object Main {

  def main(args: Array[String]): Unit = {

    val rootBehavior = Behaviors.setup[Nothing] { context =>

//      val initialInventory = Map(
//        "product-1" -> 10,
//        "product-2" -> 0
//      )
      DatabaseInitializer.initialize()
      val inventoryRepository = new InventoryRepository
      val inventoryActor =
        context.spawn(
          InventoryActor(inventoryRepository),
          "inventory-actor"
        )

      val orderManager =
        context.spawn(
          OrderManager(inventoryActor),
          "order-manager"
        )

      val replyActor =
        context.spawnAnonymous(
          Behaviors.setup[OrderResult] { replyContext =>
            Behaviors.receiveMessage { result =>
              replyContext.log.info(s"Order result received: $result")
              Behaviors.same
            }
          }
        )

      // Send test order
//      orderManager ! OrderManager.CreateOrder(
//        productId = "product-1",
//        quantity = 2,
//        replyTo = replyActor
//      )

      // Fire multiple orders concurrently
      (1 to 5).foreach { i =>
        orderManager ! OrderManager.CreateOrder(
          productId = "product-1",
          quantity = 2,
          replyTo = replyActor
        )
      }

      Behaviors.empty
    }

    ActorSystem[Nothing](rootBehavior, "OrderProcessingSystem")
  }
}