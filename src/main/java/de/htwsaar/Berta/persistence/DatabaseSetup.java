package de.htwsaar.Berta.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseSetup {
  public static void main(String[] args) {
    String url = "jdbc:sqlite:database.db";
    String sql = """
      CREATE TABLE IF NOT EXISTS games (
          id INTEGER PRIMARY KEY AUTOINCREMENT,
          steam_id INTEGER NOT NULL UNIQUE,
          name TEXT NOT NULL,
          price_cents INTEGER,
          image_url TEXT
          );
    """;

    try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()) {
      stmt.execute(sql);
      System.out.println("Table 'games' created successfully.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
