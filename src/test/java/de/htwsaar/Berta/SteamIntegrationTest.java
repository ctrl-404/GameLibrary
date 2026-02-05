package de.htwsaar.Berta;

import de.htwsaar.Berta.persistence.GameDTO;
import de.htwsaar.Berta.servicelayer.SteamIntegration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit-Tests für die Steam-Anbindung.
 */
class SteamIntegrationTest {

    private SteamIntegration steamIntegration;

    @BeforeEach
    void setUp() {
        steamIntegration = new SteamIntegration();
    }

    @Test
    void testEmptySearchTermReturnsEmptyList() {
        List<GameDTO> result = steamIntegration.fetchGameList("");
        assertTrue(result.isEmpty(), "Empty Searchrequest should result in empty Gamelist");
    }

    @Test
    void testNullSearchTermReturnsEmptyList() {
        List<GameDTO> result = steamIntegration.fetchGameList(null);
        assertTrue(result.isEmpty(), "Null input should result in empty Gamelist");
    }
}
