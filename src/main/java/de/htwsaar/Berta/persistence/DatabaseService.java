package de.htwsaar.Berta.persistence;

import java.util.List;

/**
 * Schnittstelle für Datenbankoperationen bezüglich der Spieleverwaltung.
 */
public interface DatabaseService extends AutoCloseable {

    /**
     * Speichert ein Spiel in der Datenbank oder aktualisiert es, falls es bereits existiert.
     *
     * @param dto Das zu speichernde Spiel.
     */
    void saveGameToDatabase(GameDTO dto);

    /**
     * Entfernt ein Spiel aus der Datenbank.
     *
     * @param dto Das zu entfernende Spiel.
     */
    void removeGameFromDatabase(GameDTO dto);

    /**
     * Ruft alle gespeicherten Spiele aus der Datenbank ab.
     *
     * @return Eine Liste aller gespeicherten Spiele.
     */
    List<GameDTO> getAllGames();

    /**
     * Schließt die Datenbankverbindung.
     */
    @Override
    void close();
}

