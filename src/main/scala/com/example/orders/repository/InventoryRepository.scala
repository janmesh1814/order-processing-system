package com.example.orders.repository

import com.example.orders.config.DatabaseConfig

class InventoryRepository {

  def reserveStock(productId: String, quantity: Int): Boolean = {
    val conn = DatabaseConfig.getConnection()
    try {
      val call = conn.prepareCall("{ CALL reserve_stock(?, ?, ?) }")
      call.setString(1, productId)
      call.setInt(2, quantity)
      call.registerOutParameter(3, java.sql.Types.BOOLEAN)
      call.execute()
      call.getBoolean(3)
    } finally {
      conn.close()
    }
  }
}