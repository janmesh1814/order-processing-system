package com.example.orders.domain

case class Order(
                  id: String,
                  productId: String,
                  quantity: Int,
                  status: OrderStatus
                )