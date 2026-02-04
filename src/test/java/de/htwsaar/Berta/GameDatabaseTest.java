package de.htwsaar.Berta;

import de.htwsaar.Berta.persistence.GameDTO;
import de.htwsaar.Berta.persistence.GameDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integrationstests für die Datenbank-Operationen.
 */
class GameDatabaseTest {

    private GameDatabase db;

    @BeforeEach
    void setUp() throws SQLException {
        db = new GameDatabase();
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    void testSaveAndRetrieveGame() {
        GameDTO newGame = new GameDTO("Test Game", 123, "url", 1999);

        db.saveGameToDatabase(newGame);
        List<GameDTO> allGames = db.getAllGames();

        boolean found = allGames.stream()
                .anyMatch(g -> g.steamId() == 123 && g.name().equals("Test Game"));

        assertTrue(found, "Saved games should be inside the list.");
    }

    @Test
    void testRemoveGame() {
        GameDTO game = new GameDTO("Delete Me", 999, "url", 0);
        db.saveGameToDatabase(game);

        db.removeGameFromDatabase(game);
        List<GameDTO> allGames = db.getAllGames();

        boolean found = allGames.stream().anyMatch(g -> g.steamId() == 999);
        assertFalse(found, "Games shouldn't be inside the Database after removing");
    }
}
