package com.example.orders.repository

trait InventoryRepository {

  def getQuantity(productId: String): Int

  def updateQuantity(
                      productId: String,
                      newQuantity: Int
                    ): Unit
}