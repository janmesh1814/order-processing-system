package com.example.orders.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.example.orders.domain.{Order, OrderStatus}
import com.example.orders.actors.InventoryActor._

object OrderActor {
  sealed trait Command

  final case class ProcessOrder(
                                 orderId: String,
                                 productId: String,
                                 quantity: Int,
                                 replyTo: ActorRef[OrderResult]
                               ) extends Command

  private final case class InventoryResponseWrapped(
                                                     response: StockResponse
                                                   ) extends Command

  sealed trait OrderResult
  final case class OrderConfirmed(order: Order) extends OrderResult
  final case class OrderRejected(reason: String) extends OrderResult

  def apply(
             inventoryActor: ActorRef[InventoryActor.Command]
           ): Behavior[Command] =
    Behaviors.setup { context =>
      context.log.info("OrderActor started")

      Behaviors.receiveMessage {
        case cmd: ProcessOrder =>
          inventoryActor ! CheckAndReserveStock(
            cmd.productId,
            cmd.quantity,
            context.messageAdapter(InventoryResponseWrapped)
          )

          waitingForInventory(cmd)
      }
    }

  private def waitingForInventory(
                                   cmd: ProcessOrder
                                 ): Behavior[Command] =
    Behaviors.receive { (context, message) =>
      message match {

        case InventoryResponseWrapped(StockReserved) =>
          val order = Order(
            id = cmd.orderId,
            productId = cmd.productId,
            quantity = cmd.quantity,
            status = OrderStatus.Confirmed
          )

          context.log.info(s"Order ${cmd.orderId} confirmed")

          cmd.replyTo ! OrderConfirmed(order)

          Behaviors.stopped

        case InventoryResponseWrapped(OutOfStock) =>
          context.log.warn(s"Order ${cmd.orderId} rejected - out of stock")

          cmd.replyTo ! OrderRejected("Out of stock")

          Behaviors.stopped
      }
    }
}