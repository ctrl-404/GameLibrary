package de.htwsaar.Berta.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Hilfsklasse zur initialen Einrichtung der Datenbankstruktur.
 */
public class DatabaseSetup {

  public static final String DB_URL = "jdbc:sqlite:database.db";

  private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS games (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                steam_id INTEGER NOT NULL UNIQUE,
                name TEXT NOT NULL,
                price_cents INTEGER,
                image_url TEXT
            );
            """;

  /**
   * Erstellt die Datenbankdatei und das Schema, falls noch nicht vorhanden.
   */
  public static void init() {
    try (Connection conn = DriverManager.getConnection(DB_URL);
         Statement stmt = conn.createStatement()) {

      stmt.execute(CREATE_TABLE_SQL);
      System.out.println("Datenbank wurde erfolgreich initialisiert (Pfad: ./database.db)");

    } catch (Exception e) {
      System.err.println("Fehler bei der Datenbank-Initialisierung: " + e.getMessage());
      e.printStackTrace();
    }
  }
}