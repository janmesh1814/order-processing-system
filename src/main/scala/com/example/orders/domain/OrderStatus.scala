package com.example.orders.domain

sealed trait OrderStatus

object OrderStatus {
  case object Created extends OrderStatus
  case object Confirmed extends OrderStatus
  case object Rejected extends OrderStatus
}