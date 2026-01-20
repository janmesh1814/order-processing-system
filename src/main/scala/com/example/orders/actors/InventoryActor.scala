package com.example.orders.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object InventoryActor {
  sealed trait Command

  final case class CheckAndReserveStock(
                                         productId: String,
                                         quantity: Int,
                                         replyTo: ActorRef[StockResponse]
                                       ) extends Command

  sealed trait StockResponse
  case object StockReserved extends StockResponse
  case object OutOfStock extends StockResponse

  def apply(initialInventory: Map[String, Int]): Behavior[Command] =
    Behaviors.setup { context =>
      context.log.info("InventoryActor started")

      active(initialInventory)
    }

  private def active(inventory: Map[String, Int]): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {

        case CheckAndReserveStock(productId, quantity, replyTo) =>

          val currentStock = inventory.getOrElse(productId, 0)

          if (currentStock >= quantity) {
            Thread.sleep(300) // for concurrency
            context.log.info(
              s"Stock available for product=$productId, reserving $quantity"
            )

            val updatedInventory =
              inventory.updated(productId, currentStock - quantity)

            replyTo ! StockReserved

            active(updatedInventory)
          } else {
            context.log.warn(
              s"Out of stock for product=$productId, requested=$quantity, available=$currentStock"
            )

            replyTo ! OutOfStock

            Behaviors.same
          }
      }
    }
}