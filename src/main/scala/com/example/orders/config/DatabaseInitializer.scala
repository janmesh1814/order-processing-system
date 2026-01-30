package com.example.orders.config

import java.sql.Connection
import scala.io.Source

object DatabaseInitializer {

  def initialize(): Unit = {
    val conn = DatabaseConfig.getConnection()
    try {
      executeSimpleSqlFile(conn, "db/schema.sql")
      executeDropProcedure(conn)
      executeProcedure(conn, "db/procedures.sql")
      executeSimpleSqlFile(conn, "db/data.sql")
    } finally {
      conn.close()
    }
  }

  // For CREATE TABLE, INSERT etc.
  private def executeSimpleSqlFile(
                                    conn: Connection,
                                    resourcePath: String
                                  ): Unit = {
    val sqlFile =
      Source.fromResource(resourcePath).getLines().mkString("\n")

    sqlFile
      .split(";")
      .map(_.trim)
      .filter(_.nonEmpty)
      .foreach { stmt =>
        conn.createStatement().execute(stmt)
      }
  }

  // Execute DROP separately (single statement)
  private def executeDropProcedure(conn: Connection): Unit = {
    conn.createStatement().execute(
      "DROP PROCEDURE IF EXISTS reserve_stock"
    )
  }

  // Execute CREATE PROCEDURE as ONE statement
  private def executeProcedure(
                                conn: Connection,
                                resourcePath: String
                              ): Unit = {
    val procedureSql =
      Source.fromResource(resourcePath).getLines().mkString("\n")

    conn.createStatement().execute(procedureSql)
  }
}