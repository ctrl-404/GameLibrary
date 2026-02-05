package de.htwsaar.Berta.presentation;

import static com.raylib.Raylib.*;
import de.htwsaar.Berta.persistence.DatabaseService;
import de.htwsaar.Berta.persistence.GameDatabase;
import de.htwsaar.Berta.servicelayer.SteamIntegration;
import de.htwsaar.Berta.servicelayer.UserInterface;

import java.sql.SQLException;

/**
 * Implementierung der Benutzeroberfläche mit Raylib.
 */
public class RaylibUserInterface implements UserInterface {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int POP_WIDTH = 500;
    public static final int POP_HEIGHT = 200;
    public static final int POP_X = (WIDTH - POP_WIDTH) / 2;
    public static final int POP_Y = (HEIGHT - POP_HEIGHT) / 2;

    private final DatabaseService dbService;
    private final SteamIntegration steamIntegration;

    public RaylibUserInterface() throws SQLException {
        this.dbService = new GameDatabase();
        this.steamIntegration = new SteamIntegration();
    }

    @Override
    public void run() throws SQLException {
        InitWindow(WIDTH, HEIGHT, "Berta Game Library");
        SetTargetFPS(60);

        Screen currentScreen = new Screen(dbService.getAllGames(), false, dbService, steamIntegration);

        while (!WindowShouldClose()) {
            currentScreen = currentScreen.update();
            BeginDrawing();
            currentScreen.draw();
            EndDrawing();
        }
        close();
    }

    @Override
    public void showError(String message) {
        System.err.println("UI error: " + message);
    }

    @Override
    public void close() {
        dbService.close();
        CloseWindow();
    }
}