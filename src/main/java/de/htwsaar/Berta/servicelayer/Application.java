package de.htwsaar.Berta.servicelayer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import de.htwsaar.Berta.persistence.DatabaseService;
import de.htwsaar.Berta.persistence.GameDTO;
import de.htwsaar.Berta.persistence.GameDatabase;

public class Application {

    private DatabaseService databaseService;
    private GameService gameService;
    private Scanner scanner = new Scanner(System.in);

    public Application() throws SQLException {
        databaseService = new GameDatabase();
        gameService = new SteamIntegration();
    }

    public void run() {
        String foo = askUserInput();
        ArrayList<GameDTO> games = gameService.fetchGameList(foo);
        for (GameDTO dto : gameService.fetchGameList(foo)) {
            System.out.println("Fetched: " + dto.name() + " | Steam ID: " + dto.steamId());
            // databaseService.saveGameToDatabase(dto);
        }
        if (games.size() == 0) {
            System.out.println("No games found for the search term.");
            return;
        }
        int gameId = askForGameId();
        for (GameDTO dto : games) {
            if (dto.steamId() == gameId) {
                databaseService.saveGameToDatabase(dto);
                System.out.println("Saved " + dto.name() + " to database.");
            }
        }
    }

    public String askUserInput() {
        System.out.println("Please enter a search term: ");
        return scanner.nextLine();
    }

    public int askForGameId() {
        System.out.println("Please enter a Steam Game ID to add to the database: ");
        return Integer.parseInt(scanner.nextLine());
    }

        
}
