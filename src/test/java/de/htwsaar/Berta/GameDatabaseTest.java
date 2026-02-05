package de.htwsaar.Berta;

import de.htwsaar.Berta.persistence.GameDTO;
import de.htwsaar.Berta.persistence.GameDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integrationstests für die Datenbank-Operationen.
 */
class GameDatabaseTest {

    private GameDatabase db;
    private final String TEST_DB_FILE = "test_database.db";
    private final String TEST_DB_URL = "jdbc:sqlite:" + TEST_DB_FILE;

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS games (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                steam_id INTEGER NOT NULL UNIQUE,
                name TEXT NOT NULL,
                price_cents INTEGER,
                image_url TEXT
            );
            """;

    @BeforeEach
    void setUp() throws Exception {
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE_SQL);
        }
        db = new GameDatabase() {
        };
    }

    @AfterEach
    void tearDown() {
        db.close();
        File dbFile = new File(TEST_DB_FILE);
        if (dbFile.exists()) {
            dbFile.delete();
        }
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
