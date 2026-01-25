package com.example.orders.config

import java.sql.{Connection, DriverManager}

object DatabaseConfig {

  Class.forName("com.mysql.cj.jdbc.Driver")

  private val Url =
    "jdbc:mysql://127.0.0.1:3306/order_system?useSSL=false&serverTimezone=UTC"

  private val User = "root"
  private val Password = ""

  def getConnection(): Connection =
    DriverManager.getConnection(Url, User, Password)
}