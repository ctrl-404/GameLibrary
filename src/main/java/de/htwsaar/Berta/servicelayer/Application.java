package de.htwsaar.Berta.servicelayer;

import java.sql.SQLException;

import de.htwsaar.Berta.persistence.DatabaseService;
import de.htwsaar.Berta.persistence.GameDTO;

public class Application {

    private DatabaseService databaseService;
    private GameService gameService;

    public Application() throws SQLException {
        databaseService = new DatabaseService();
        gameService = new SteamIntegration();
    }

    public void run() {
        String foo = "Battlefield";
        gameService.fetchGameList(foo);
        for (GameDTO dto : gameService.fetchGameList(foo)) {
            System.out.println("Fetched: " + dto.name() + " | Steam ID: " + dto.steamId());
            // databaseService.saveGameToDatabase(dto);
        }
    }
}
