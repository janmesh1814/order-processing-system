package com.example.orders.actors

import akka.actor.typed.{ActorRef, Behavior, DispatcherSelector}
import akka.actor.typed.scaladsl.Behaviors
import com.example.orders.repository.InventoryRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object InventoryActor {
  sealed trait Command

  final case class CheckAndReserveStock(
                                         productId: String,
                                         quantity: Int,
                                         replyTo: ActorRef[StockResponse]
                                       ) extends Command

  private final case class WrappedDbResult(
                                            success: Boolean,
                                            replyTo: ActorRef[StockResponse]
                                          ) extends Command

  sealed trait StockResponse
  case object StockReserved extends StockResponse
  case object OutOfStock extends StockResponse

  def apply(
             inventoryRepository: InventoryRepository
           ): Behavior[Command] =
    Behaviors.setup { context =>

      implicit val ec: ExecutionContext =
        context.system.dispatchers.lookup(
          DispatcherSelector.fromConfig("akka.actor.blocking-dispatcher")
        )

      Behaviors.receiveMessage {

        case CheckAndReserveStock(productId, quantity, replyTo) =>

          context.pipeToSelf(
            Future {
              inventoryRepository.reserveStock(productId, quantity)
            }
          ) {
            case Success(result) =>
              WrappedDbResult(result, replyTo)
            case Failure(_) =>
              WrappedDbResult(false, replyTo)
          }

          Behaviors.same

        case WrappedDbResult(true, replyTo) =>
          replyTo ! StockReserved
          Behaviors.same

        case WrappedDbResult(false, replyTo) =>
          replyTo ! OutOfStock
          Behaviors.same
      }
    }
}