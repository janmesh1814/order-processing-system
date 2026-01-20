package com.example.orders.steps

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import com.example.orders.actors._
import com.example.orders.actors.OrderActor.OrderResult
import io.cucumber.scala.{EN, ScalaDsl}

class OrderProcessingSteps extends ScalaDsl with EN {

  private val testKit = ActorTestKit()

  private var orderResult: Option[OrderResult] = None
  private var inventoryData: Map[String, Int] = Map.empty

  // GIVEN
  Given(
    """inventory has (\d+) quantity of product with id "([^"]+)""""
  ) { (quantity: Int, productId: String) =>
    inventoryData += (productId -> quantity)
  }

  // WHEN
  When(
    """user tries to place order for product with id "([^"]+)" with quantity (\d+)"""
  ) { (productId: String, quantity: Int) =>

    val inventoryActor =
      testKit.spawn(InventoryActor(inventoryData))

    val orderManager =
      testKit.spawn(OrderManager(inventoryActor))

    val probe = testKit.createTestProbe[OrderResult]()

    orderManager ! OrderManager.CreateOrder(
      productId = productId,
      quantity = quantity,
      replyTo = probe.ref
    )

    orderResult = Some(probe.receiveMessage())
  }

  // THEN (ACCEPT)
  Then("""Accept the order""") { () =>
    println(s"Order result = $orderResult")
    assert(orderResult.exists(_.isInstanceOf[OrderActor.OrderConfirmed]))
  }

  // THEN (REJECT)
  Then("""Reject the order""") { () =>
    println(s"Order result = $orderResult")
    assert(orderResult.exists(_.isInstanceOf[OrderActor.OrderRejected]))
  }
}