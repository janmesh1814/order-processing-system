package com.example.orders.actors

import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import com.example.orders.actors.OrderActor.{OrderResult, ProcessOrder}

import java.util.UUID

object OrderManager {
  sealed trait Command

  final case class CreateOrder(
                                productId: String,
                                quantity: Int,
                                replyTo: ActorRef[OrderResult]
                              ) extends Command

  def apply(
             inventoryActor: ActorRef[InventoryActor.Command]
           ): Behavior[Command] =
    Behaviors.setup { context =>
      context.log.info("OrderManager started")

      Behaviors.receiveMessage {
        case CreateOrder(productId, quantity, replyTo) =>
          val orderId = UUID.randomUUID().toString

          context.log.info(s"Creating order $orderId")

          val orderActor =
            context.spawn(
              OrderActor(inventoryActor),
              s"order-$orderId"
            )

          orderActor ! ProcessOrder(
            orderId = orderId,
            productId = productId,
            quantity = quantity,
            replyTo = replyTo
          )

          Behaviors.same
      }
    }
}
