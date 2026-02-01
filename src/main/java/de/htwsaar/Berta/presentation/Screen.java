package de.htwsaar.Berta.presentation;

import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import static de.htwsaar.Berta.presentation.Padding.PADDING_TEXT;
import static de.htwsaar.Berta.presentation.Textsize.*;

import de.htwsaar.Berta.persistence.GameDTO;
import de.htwsaar.Berta.servicelayer.Application;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Screen {

    private final ScreenManager screenManager;
    private final ArrayList<GameDTO> games;

    private int selectedIndex = 0;
    private final int itemsPerPage = 10;

    private boolean isSearchResult;
    private boolean isSearchOpen = false; // Replaces typingMode/searchBarFocused
    private final StringBuilder searchRequest = new StringBuilder();

    private boolean searchBarFocused = false;

    public Screen(ScreenManager manager, List<GameDTO> games, boolean isSearchResult) {
        this.screenManager = manager;
        this.games = new ArrayList<>(games);
        this.isSearchResult = isSearchResult;
    }

    public void update() {
        if (isSearchOpen) {
            handleSearchInput();
        } else {
            handleNavigation();
            if (IsKeyPressed(KEY_S) || IsKeyPressed(KEY_F)) {
                isSearchOpen = true;
                searchRequest.setLength(0);
            }
        }
    }

    private void handleSearchInput() {
        if (IsKeyPressed(KEY_C)) {
            isSearchOpen = false;
            return;
        }
        int key = GetCharPressed();
        while (key > 0) {
            if ((key >= 32) && (key <= 125) && (searchRequest.length() < 30)) {
                searchRequest.append((char) key);
            }
            key = GetCharPressed();
        }
        if (IsKeyPressed(KEY_BACKSPACE) && !searchRequest.isEmpty()) {
            searchRequest.setLength(searchRequest.length() - 1);
        }
        if (IsKeyPressed(KEY_ENTER) && !searchRequest.isEmpty()) {
            performSearch();
        }
    }

    private void performSearch() {
        isSearchOpen = false;
        System.out.println("Searching for: " + searchRequest);

        try {
            Application app = new Application();
            ArrayList<GameDTO> fetched = app.gameService.fetchGameList(searchRequest.toString());
            Screen resultScreen = new Screen(screenManager, fetched, true);
            screenManager.setScreen(resultScreen);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void draw() {
        ClearBackground(DARKGRAY);
        DrawText("GameLibrary - Berta", PADDING_TEXT, PADDING_TEXT, HEADER_SIZE, LIGHTGRAY);
        if (games.isEmpty()) {
            DrawText("No games found. Press 'S' to search.", PADDING_TEXT, 100, PARAGRAPH_SIZE, GRAY);
        } else {
            drawList();
            drawPaginationInfo();
        }
        if (!isSearchOpen) {
            DrawText("[S] Search", WindowManager.WIDTH - 150, WindowManager.HEIGHT - 50, 20, LIGHTGRAY);
            if(isSearchResult){
                DrawText("[H] Home", WindowManager.WIDTH - 150, WindowManager.HEIGHT - (50 + PADDING_TEXT), 20, LIGHTGRAY);
            }
        }
        if (isSearchOpen) {
            drawSearchPopup();
        }
    }

    private void drawSearchPopup() {
        DrawRectangle(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT, Fade(BLACK, 0.5f));
        int popupWidth = 500;
        int popupHeight = 200;
        int x = (WindowManager.WIDTH - popupWidth) / 2; // Center X
        int y = (WindowManager.HEIGHT - popupHeight) / 2; // Center Y

        DrawRectangle(x, y, popupWidth, popupHeight, RAYWHITE);
        DrawRectangleLines(x, y, popupWidth, popupHeight, DARKGRAY);
        DrawText("Search Steam", x + 20, y + 20, 20, DARKGRAY);

        int inputBoxY = y + 60;

        DrawRectangle(x + 20, inputBoxY, popupWidth - 40, 40, LIGHTGRAY);
        DrawRectangleLines(x + 20, inputBoxY, popupWidth - 40, 40, DARKGRAY);
        DrawText(searchRequest.toString(), x + 30, inputBoxY + 10, 20, BLACK);

        if ((GetTime() * 2) % 2 > 1) {
            int textWidth = MeasureText(searchRequest.toString(), 20);
            DrawText("_", x + 30 + textWidth, inputBoxY + 10, 20, BLACK);
        }
        DrawText("Press ENTER to Search", x + 20, y + 120, 15, GRAY);
        DrawText("Press [C] to Cancel", x + 20, y + 145, 15, GRAY);
    }

    private void returnToHome() {
        List<GameDTO> allGames = screenManager.getDbService().getAllGames();
        Screen homeScreen = new Screen(screenManager, allGames, false);
        screenManager.setScreen(homeScreen);
    }

    private void handleNavigation() {
        if (!searchBarFocused) {
            if (IsKeyPressed(KEY_DOWN) && selectedIndex < games.size() - 1) {
                selectedIndex++;
            }
            if(IsKeyPressed(KEY_S)){
                searchBarFocused = true;
            }
            if(IsKeyPressed(KEY_H)){
                returnToHome();
            }
            if (IsKeyPressed(KEY_UP)) {
                if (selectedIndex > 0) {
                    selectedIndex--;
                } else {
                    searchBarFocused = true;
                }
            }
        } else {
            if (IsKeyPressed(KEY_DOWN)) {
                searchBarFocused = false;
            }
        }
    }

    private void handleSelection() {
        if (IsKeyPressed(KEY_ENTER) || IsKeyPressed(KEY_KP_ENTER)) {
            if (!games.isEmpty() && !searchBarFocused) {
                GameDTO chosenGame = games.get(selectedIndex);
                screenManager.getDbService().saveGameToDatabase(chosenGame);
                System.out.println("Saved via Enter: " + chosenGame.name());
            }
        }
    }

    private void drawList() {
        int startDisplayIndex = (selectedIndex / itemsPerPage) * itemsPerPage;
        int endDisplayIndex = Math.min(startDisplayIndex + itemsPerPage, games.size());

        for (int i = startDisplayIndex; i < endDisplayIndex; i++) {
            int localIndex = i - startDisplayIndex;
            int yPos = (2 * PADDING_TEXT + 10) + (localIndex * (2 * PADDING_TEXT + 10));
            if (i == selectedIndex && !searchBarFocused) {
                DrawRectangle(PADDING_TEXT, yPos, WindowManager.WIDTH - 2 * PADDING_TEXT, 2 * PADDING_TEXT, GRAY);
                String gameString = String.format(">  %s -- %d ct", games.get(i).name(), games.get(i).price());
                DrawText(gameString, PADDING_TEXT + 10, yPos + 10, HIGHLIGHTED_SIZE, GREEN);
            } else {
                String gameString = String.format("%s -- %d ct", games.get(i).name(), games.get(i).price());
                DrawText(gameString, PADDING_TEXT + 10, yPos + 10, PARAGRAPH_SIZE, RAYWHITE);
            }
        }
    }

    private void drawPaginationInfo() {
        int totalPages = (int) Math.ceil((double) games.size() / itemsPerPage);
        int currentPage = (selectedIndex / itemsPerPage) + 1;
        String pageText = String.format("Page %d of %d (Total: %d)", currentPage, totalPages, games.size());
        DrawText(pageText, PADDING_TEXT, WindowManager.HEIGHT - 30, 15, RAYWHITE);
    }
}