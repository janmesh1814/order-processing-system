package com.example.orders

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import com.example.orders.actors._
import com.example.orders.actors.OrderActor._
import io.cucumber.scala.{EN, ScalaDsl}
import org.junit.Assert._

class OrderProcessingSteps extends ScalaDsl with EN {

  private val testKit = ActorTestKit()

  private var inventory: Map[String, Int] = Map.empty
  private var orderResult: OrderResult = _

  // GIVEN
  Given(
    """inventory has {int} quantity of product with id "{word}""""
  ) { (qty: Int, productId: String) =>
    inventory = Map(productId -> qty)
  }

  // WHEN
  When(
    """user tries to place order for product with id "{word}" with quantity {int}"""
  ) { (productId: String, quantity: Int) =>

    val inventoryActor =
      testKit.spawn(InventoryActor(inventory))

    val orderManager =
      testKit.spawn(OrderManager(inventoryActor))

    val probe = testKit.createTestProbe[OrderResult]()

    orderManager ! OrderManager.CreateOrder(
      productId = productId,
      quantity = quantity,
      replyTo = probe.ref
    )

    orderResult = probe.receiveMessage()
  }

  // THEN
  Then("""Accept the order""") { () =>
    assertTrue(orderResult.isInstanceOf[OrderConfirmed])
  }

  Then("""Reject the order""") { () =>
    assertTrue(orderResult.isInstanceOf[OrderRejected])
  }
}