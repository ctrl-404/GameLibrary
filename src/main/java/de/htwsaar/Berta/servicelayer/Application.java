package de.htwsaar.Berta.servicelayer;

import java.sql.SQLException;
import java.util.ArrayList;

import de.htwsaar.Berta.persistence.DatabaseService;
import de.htwsaar.Berta.persistence.GameDTO;

public class Application {

    private DatabaseService databaseService;
    private GameService gameService;

    public Application() throws SQLException {
        databaseService = new DatabaseService();
        gameService = new SteamIntegration();
    }

    public static void main(String[] args) {
        String foo = "Valheim";

        try {
            Application app = new Application();
            ArrayList<GameDTO> fetched = app.gameService.fetchGameList(foo);
            for (GameDTO dto : fetched) {
                System.out.println("Fetched: " + dto.name() + " | Steam ID: " + dto.steamId());
                // app.databaseService.saveGameToDatabase(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    
}
