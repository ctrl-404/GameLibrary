package de.htwsaar.Berta;

import de.htwsaar.Berta.persistence.GameDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests für das GameDTO Record.
 */
class GameDTOTest {

    @Test
    void testRecordState() {
        GameDTO game = new GameDTO("Portal 2", 620, "http://image.url", 999);

        assertAll("Testing DTO",
                () -> assertEquals("Portal 2", game.name()),
                () -> assertEquals(620, game.steamId()),
                () -> assertEquals("http://image.url", game.imageUrl()),
                () -> assertEquals(999, game.price())
        );
    }
}
