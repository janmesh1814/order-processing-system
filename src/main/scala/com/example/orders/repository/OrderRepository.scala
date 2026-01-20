package com.example.orders.repository

import com.example.orders.domain.Order

trait OrderRepository {

  def save(order: Order): Unit
}