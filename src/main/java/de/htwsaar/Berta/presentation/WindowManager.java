package de.htwsaar.Berta.presentation;

import de.htwsaar.Berta.persistence.DatabaseService;
import de.htwsaar.Berta.persistence.GameDTO;

import java.sql.SQLException;
import java.util.List;

import static com.raylib.Raylib.*;

public class WindowManager {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final ScreenManager screenManager;

    public WindowManager(DatabaseService dbService) throws SQLException {
        InitWindow(WIDTH, HEIGHT, "Berta Game Library");
        SetTargetFPS(60);

        this.screenManager = new ScreenManager(dbService);
        List<GameDTO> initialGames = dbService.getAllGames();

        Screen homeScreen = new Screen(screenManager, initialGames, false);
        screenManager.setScreen(homeScreen);
    }

    public void run() {
        while (!WindowShouldClose()) {
            screenManager.update();
            BeginDrawing();
            screenManager.draw();
            EndDrawing();
        }
        CloseWindow();
    }
}