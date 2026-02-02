package de.htwsaar.Berta.presentation;

import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import static de.htwsaar.Berta.presentation.Padding.PADDING;
import static de.htwsaar.Berta.presentation.Textsize.*;
import static de.htwsaar.Berta.presentation.WindowManager.*;

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

    private final boolean isSearchResult;
    private final StringBuilder searchRequest = new StringBuilder();

    private boolean isSearchOpen = false;
    private boolean searchBarFocused = false;
    private boolean isDetailOpen = false;

    private GameDTO selectedGame = null;

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
            if (IsKeyPressed(KEY_S)) {
                isSearchOpen = true;
                searchRequest.setLength(0);
            }
        }
    }

    private void handleSearchInput() {
        if (IsKeyPressed(KEY_DOWN)) {
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
        DrawText("GameLibrary - Berta", PADDING, PADDING, HEADER_SIZE, RAYWHITE);
        if (games.isEmpty()) {
            DrawText("No games found. Press 'S' to search.", PADDING, 100, PARAGRAPH_SIZE, GRAY);
        } else {
            drawList();
            drawPaginationInfo();
        }
        if (!isSearchOpen) {
            DrawText("[S] Search", WindowManager.WIDTH - 150, WindowManager.HEIGHT - 50, 20, LIGHTGRAY);
            if (isSearchResult) {
                DrawText("[H] Home", WindowManager.WIDTH - 150, WindowManager.HEIGHT - (50 + PADDING), 20, LIGHTGRAY);
            }
        }
        if (isSearchOpen) {
            drawPopup();
            drawSearchPopup();
        }
        if (isDetailOpen && selectedGame != null) {
            drawPopup();
            drawDetailPopup();
        }
    }

    private void drawPopup() {
        DrawRectangle(0, 0, WindowManager.WIDTH, WindowManager.HEIGHT, Fade(BLACK, 0.5f));
        DrawRectangle(POP_X, POP_Y, POP_WIDTH, POP_HEIGHT, RAYWHITE);
        DrawRectangleLines(POP_X, POP_Y, POP_WIDTH, POP_HEIGHT, DARKGRAY);
    }

    private void drawSearchPopup() {
        DrawText("Search Steam", POP_X + PADDING, POP_Y + PADDING, 20, DARKGRAY);

        int inputBoxY = POP_Y + 60;

        DrawRectangle(POP_X + 20, inputBoxY, POP_WIDTH - 40, 40, LIGHTGRAY);
        DrawRectangleLines(POP_X + 20, inputBoxY, POP_WIDTH - 40, 40, DARKGRAY);
        DrawText(searchRequest.toString(), POP_X + 30, inputBoxY + 10, 20, BLACK);

        if ((GetTime() * 2) % 2 > 1) {
            int textWidth = MeasureText(searchRequest.toString(), 20);
            DrawText("_", POP_X + 30 + textWidth, inputBoxY + 10, HIGHLIGHTED_SIZE, BLACK);
        }
        DrawText("Press [ENTER] to Search", POP_X + PADDING, POP_Y + 120, PARAGRAPH_SIZE, GRAY);
        DrawText("Press [DOWN] to Cancel", POP_X + PADDING, POP_Y + 145, PARAGRAPH_SIZE, GRAY);
    }

    private void drawDetailPopup() {
        DrawText("Game Details", POP_X + PADDING, POP_Y + PADDING, HEADER_SIZE, DARKGRAY);

        String nameText = String.format("Name: %1.20s ...", selectedGame.name());
        String priceText = "Price: " + (selectedGame.price() / 100.0) + ",-";
        String steamID = "ID: " + selectedGame.steamId();

        DrawText(steamID, POP_X + PADDING, POP_Y + 70, HIGHLIGHTED_SIZE, BLACK);
        DrawText(nameText, POP_X + PADDING, POP_Y + 100, HIGHLIGHTED_SIZE, BLACK);
        DrawText(priceText, POP_X + PADDING, POP_Y + 130, HIGHLIGHTED_SIZE, BLACK);

        DrawText("Press [DOWN] to Close", POP_X + PADDING, POP_Y + POP_HEIGHT - 30, PARAGRAPH_SIZE, GRAY);
        if (isSearchResult) {
            DrawText("Press [C] to Save", POP_X + PADDING + 200, POP_Y + POP_HEIGHT - 30, PARAGRAPH_SIZE, GRAY);
        }else{
            DrawText("Press [R] to Remove", POP_X + PADDING + 200, POP_Y + POP_HEIGHT - 30, PARAGRAPH_SIZE, GRAY);
        }
    }

    private void returnToHome() {
        List<GameDTO> allGames = screenManager.getDbService().getAllGames();
        Screen homeScreen = new Screen(screenManager, allGames, false);
        screenManager.setScreen(homeScreen);
    }

    private void handleNavigation() {
        if (!searchBarFocused) {
            scroll();
            handleSelection();
        } else {
            if (IsKeyPressed(KEY_DOWN)) {
                searchBarFocused = false;
            }
        }
    }

    private void scroll(){
        if (IsKeyPressed(KEY_DOWN) && selectedIndex < games.size() - 1) {
            selectedIndex++;
        }
        if (IsKeyPressed(KEY_UP)) {
            if (selectedIndex > 0) {
                selectedIndex--;
            } 
        }
    }

    private void handleSelection() {
        if (IsKeyPressed(KEY_ENTER) || IsKeyPressed(KEY_KP_ENTER)) {
            if (!games.isEmpty()) {
                selectedGame = games.get(selectedIndex);
                isDetailOpen = true;
            }
        }
        if (isDetailOpen && IsKeyPressed(KEY_C) && isSearchResult){
            isDetailOpen = false;
            GameDTO chosenGame = games.get(selectedIndex);
            screenManager.getDbService().saveGameToDatabase(chosenGame);
            System.out.println("Saved via Enter: " + chosenGame.name());
        }
        if (isDetailOpen && IsKeyPressed(KEY_R) && !isSearchResult){
            isDetailOpen = false;
            GameDTO chosenGame = games.get(selectedIndex);
            screenManager.getDbService().removeGameFromDatabase(chosenGame);
            returnToHome();
            System.out.println("Removed: " + chosenGame.name());
        }
        if (isDetailOpen && IsKeyPressed(KEY_DOWN)) {
            isDetailOpen = false;
            searchBarFocused = false;
        }
        if(!isDetailOpen && IsKeyPressed(KEY_S)){
            isDetailOpen = false;
            searchBarFocused = true;
        }
        if(IsKeyPressed(KEY_H)){
            isDetailOpen = false;
            searchBarFocused = false;
            returnToHome();
        }
    }

    private void drawList() {
        int startDisplayIndex = (selectedIndex / itemsPerPage) * itemsPerPage;
        int endDisplayIndex = Math.min(startDisplayIndex + itemsPerPage, games.size());

        for (int i = startDisplayIndex; i < endDisplayIndex; i++) {
            int localIndex = i - startDisplayIndex;
            int yPos = (2 * PADDING + 10) + (localIndex * (2 * PADDING + 10));
            if (i == selectedIndex && !searchBarFocused) {
                DrawRectangle(PADDING, yPos, WindowManager.WIDTH - 2 * PADDING, 2 * PADDING, GRAY);
                String gameString = String.format(">  %s -- %.2f,-", games.get(i).name(), games.get(i).price()/100.0);
                DrawText(gameString, PADDING + 10, yPos + 10, HIGHLIGHTED_SIZE, GREEN);
            } else {
                String gameString = String.format("%s -- %.2f,-", games.get(i).name(), games.get(i).price()/100.0);
                DrawText(gameString, PADDING + 10, yPos + 10, PARAGRAPH_SIZE, GRAY);
            }
        }
    }

    private void drawPaginationInfo() {
        int totalPages = (int) Math.ceil((double) games.size() / itemsPerPage);
        int currentPage = (selectedIndex / itemsPerPage) + 1;
        String pageText = String.format("Page %d of %d (Total: %d)", currentPage, totalPages, games.size());
        DrawText(pageText, PADDING, WindowManager.HEIGHT - 30, 15, RAYWHITE);
    }
}