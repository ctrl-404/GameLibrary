package de.htwsaar.Berta.presentation;

import de.htwsaar.Berta.persistence.DatabaseService;

public class ScreenManager {
    private Screen currentScreen;
    private final DatabaseService dbService;

    public ScreenManager(DatabaseService dbService) {
        this.dbService = dbService;
    }

    public void setScreen(Screen screen) {
        this.currentScreen = screen;
    }

    public void update() {
        if (currentScreen != null) {
            currentScreen.update();
        }
    }

    public void draw() {
        if (currentScreen != null) {
            currentScreen.draw();
        }
    }

    public DatabaseService getDbService() {
        return dbService;
    }
}
