package de.htwsaar.Berta.servicelayer;

import de.htwsaar.Berta.presentation.RaylibUserInterface;
import java.sql.SQLException;

/**
 * Hauptklasse der Anwendung (Einstiegspunkt).
 */
public class Application {

  public Application() {}

  /**
   * Startet die Anwendung und initialisiert die UI.
   */
  public void run() {
    try {
      UserInterface ui = new RaylibUserInterface();
      ui.run();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}