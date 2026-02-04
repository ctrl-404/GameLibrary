package de.htwsaar.Berta.servicelayer;

import java.sql.SQLException;

/**
 * Schnittstelle für die Benutzeroberfläche der Anwendung.
 */
public interface UserInterface {

    /**
     * Startet die Hauptschleife der Benutzeroberfläche.
     * @throws SQLException Bei Datenbankfehlern während des Starts.
     */
    void run() throws SQLException;

    /**
     * Zeigt eine Fehlermeldung an.
     * @param message Die Nachricht.
     */
    void showError(String message);

    /**
     * Schließt die Benutzeroberfläche und gibt Ressourcen frei.
     */
    void close();
}