package de.htwsaar.Berta.presentation;

import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;
import static de.htwsaar.Berta.presentation.Padding.PADDING;
import static de.htwsaar.Berta.presentation.Textsize.*;
import static de.htwsaar.Berta.presentation.RaylibUserInterface.*;

import de.htwsaar.Berta.persistence.DatabaseService;
import de.htwsaar.Berta.persistence.GameDTO;
import de.htwsaar.Berta.servicelayer.GameService;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert einen einzelnen Bildschirmzustand (State) in der UI.
 * Behandelt Eingaben und Rendering der Spieleliste.
 */
public class Screen {

    private final List<GameDTO> games;
    private final DatabaseService dbService;
    private final GameService gameService;

    private final boolean isSearchResult;
    private final StringBuilder searchRequest = new StringBuilder();
    private static final int ITEMS_PER_PAGE = 10;

    // UI State Flags
    private int selectedIndex = 0;
    private boolean searchBarFocused = false;
    private boolean isDetailOpen = false;
    private static boolean isSearchOpen = false;
    private GameDTO selectedGame = null;

    /**
     * Erstellt einen neuen Screen.
     *
     * @param games          Die Liste der anzuzeigenden Spiele.
     * @param isSearchResult True, wenn dies eine Suchergebnisliste ist.
     * @param dbService      Service für Datenbankzugriffe.
     * @param gameService    Service für API-Zugriffe.
     */
    public Screen(List<GameDTO> games, boolean isSearchResult, DatabaseService dbService, GameService gameService) {
        this.games = new ArrayList<>(games);
        this.isSearchResult = isSearchResult;
        this.dbService = dbService;
        this.gameService = gameService;
    }

    // --- Update Logik ---

    public Screen update() {
        if (isSearchOpen) {
            return handleSearchInput();
        }

        if (IsKeyPressed(KEY_S) && !isDetailOpen) {
            isSearchOpen = true;
            searchRequest.setLength(0);
            return this;
        }

        return handleNavigation();
    }

    private Screen handleSearchInput() {
        if (IsKeyPressed(KEY_DOWN)) {
            isSearchOpen = false;
            return this;
        }

        handleTyping();

        if (IsKeyPressed(KEY_ENTER) && !searchRequest.isEmpty()) {
            return performSearch();
        }
        return this;
    }

    private void handleTyping() {
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
    }

    private Screen performSearch() {
        isSearchOpen = false;
        System.out.println("Searching for: " + searchRequest);
        List<GameDTO> results = gameService.fetchGameList(searchRequest.toString());
        return new Screen(results, true, dbService, gameService);
    }

    private Screen returnToHome() {
        return new Screen(dbService.getAllGames(), false, dbService, gameService);
    }

    private Screen handleNavigation() {
        if (!searchBarFocused) {
            handleScrolling();
            return handleSelection();
        } else {
            if (IsKeyPressed(KEY_DOWN)) {
                searchBarFocused = false;
            }
            return this;
        }
    }

    private void handleScrolling() {
        if (IsKeyPressed(KEY_DOWN) && selectedIndex < games.size() - 1) {
            selectedIndex++;
        }
        if (IsKeyPressed(KEY_UP) && selectedIndex > 0) {
            selectedIndex--;
        }
    }

    private Screen handleSelection() {
        if ((IsKeyPressed(KEY_ENTER) || IsKeyPressed(KEY_KP_ENTER)) && !games.isEmpty()) {
            selectedGame = games.get(selectedIndex);
            isDetailOpen = true;
        }

        if (IsKeyPressed(KEY_H)) {
            return returnToHome();
        }

        if (isDetailOpen) {
            return handleDetailActions();
        } else if (IsKeyPressed(KEY_S)) {
            searchBarFocused = true;
        }

        return this;
    }

    private Screen handleDetailActions() {
        if (IsKeyPressed(KEY_C) && isSearchResult) {
            isDetailOpen = false;
            dbService.saveGameToDatabase(games.get(selectedIndex));
            System.out.println("Saved: " + games.get(selectedIndex).name());
        }

        if (IsKeyPressed(KEY_R) && !isSearchResult) {
            isDetailOpen = false;
            GameDTO chosenGame = games.get(selectedIndex);
            dbService.removeGameFromDatabase(chosenGame);
            System.out.println("Removed: " + chosenGame.name());
            return returnToHome();
        }

        if (IsKeyPressed(KEY_DOWN)) {
            isDetailOpen = false;
            searchBarFocused = false;
        }
        return this;
    }

    // --- Draw Logik ---

    public void draw() {
        ClearBackground(DARKGRAY);
        DrawText("GameLibrary - Berta", PADDING, PADDING, HEADER_SIZE, RAYWHITE);

        if (games.isEmpty()) {
            DrawText("No Games found. Press [S] to search.", PADDING, 100, PARAGRAPH_SIZE, GRAY);
        } else {
            drawList();
            drawPaginationInfo();
        }

        drawFooterHelp();

        if (isSearchOpen) {
            drawPopupOverlay();
            drawSearchPopup();
        }
        if (isDetailOpen && selectedGame != null) {
            drawPopupOverlay();
            drawDetailPopup();
        }
    }

    private void drawFooterHelp() {
        if (!isSearchOpen) {
            DrawText("[S] Search", WIDTH - 150, HEIGHT - 50, HIGHLIGHTED_SIZE, LIGHTGRAY);
            if (isSearchResult) {
                DrawText("[H] Home", WIDTH - 150, HEIGHT - (50 + PADDING), HIGHLIGHTED_SIZE, LIGHTGRAY);
            }
        }
    }

    private void drawPopupOverlay() {
        DrawRectangle(0, 0, WIDTH, HEIGHT, Fade(BLACK, 0.5f));
        DrawRectangle(POP_X, POP_Y, POP_WIDTH, POP_HEIGHT, RAYWHITE);
        DrawRectangleLines(POP_X, POP_Y, POP_WIDTH, POP_HEIGHT, DARKGRAY);
    }

    private void drawSearchPopup() {
        DrawText("Steam search", POP_X + PADDING, POP_Y + PADDING, 20, DARKGRAY);

        int inputBoxY = POP_Y + 60;
        DrawRectangle(POP_X + 20, inputBoxY, POP_WIDTH - 40, 40, LIGHTGRAY);
        DrawRectangleLines(POP_X + 20, inputBoxY, POP_WIDTH - 40, 40, DARKGRAY);
        DrawText(searchRequest.toString(), POP_X + 30, inputBoxY + 10, HIGHLIGHTED_SIZE, BLACK);

        if ((GetTime() * 2) % 2 > 1) {
            int textWidth = MeasureText(searchRequest.toString(), 20);
            DrawText("_", POP_X + 30 + textWidth, inputBoxY + 10, HIGHLIGHTED_SIZE, BLACK);
        }
        DrawText("Press [ENTER] to search", POP_X + PADDING, POP_Y + 120, PARAGRAPH_SIZE, GRAY);
        DrawText("Press [DOWN] to cancel", POP_X + PADDING, POP_Y + 145, PARAGRAPH_SIZE, GRAY);
    }

    private void drawDetailPopup() {
        DrawText("Gamedetails", POP_X + PADDING, POP_Y + PADDING, HEADER_SIZE, DARKGRAY);

        String safeName = selectedGame.name().length() > 20 ? selectedGame.name().substring(0, 20) + "..." : selectedGame.name();

        String nameText = String.format("Name: %s", safeName);
        String priceText = String.format("Price: %.2f,-", selectedGame.price() / 100.0);
        String steamID = "ID: " + selectedGame.steamId();

        DrawText(steamID, POP_X + PADDING, POP_Y + 70, HIGHLIGHTED_SIZE, BLACK);
        DrawText(nameText, POP_X + PADDING, POP_Y + 100, HIGHLIGHTED_SIZE, BLACK);
        DrawText(priceText, POP_X + PADDING, POP_Y + 130, HIGHLIGHTED_SIZE, BLACK);

        DrawText("Press [DOWN] to close", POP_X + PADDING, POP_Y + POP_HEIGHT - 30, PARAGRAPH_SIZE, GRAY);

        if (isSearchResult) {
            DrawText("Press [C] to safe", POP_X + PADDING + 200, POP_Y + POP_HEIGHT - 30, PARAGRAPH_SIZE, GRAY);
        } else {
            DrawText("Press [R] to delete", POP_X + PADDING + 200, POP_Y + POP_HEIGHT - 30, PARAGRAPH_SIZE, GRAY);
        }
    }

    private void drawList() {
        int startDisplayIndex = (selectedIndex / ITEMS_PER_PAGE) * ITEMS_PER_PAGE;
        int endDisplayIndex = Math.min(startDisplayIndex + ITEMS_PER_PAGE, games.size());

        for (int i = startDisplayIndex; i < endDisplayIndex; i++) {
            int localIndex = i - startDisplayIndex;
            int yPos = (2 * PADDING + 10) + (localIndex * (2 * PADDING + 10));

            String gameString = String.format("%s -- %.2f,-", games.get(i).name(), games.get(i).price() / 100.0);

            if (i == selectedIndex && !searchBarFocused) {
                DrawRectangle(PADDING, yPos, WIDTH - 2 * PADDING, 2 * PADDING, GRAY);
                DrawText("> " + gameString, PADDING + 10, yPos + 10, HIGHLIGHTED_SIZE, SKYBLUE);
            } else {
                DrawText(gameString, PADDING + 10, yPos + 10, PARAGRAPH_SIZE, GRAY);
            }
        }
    }

    private void drawPaginationInfo() {
        int totalPages = (int) Math.ceil((double) games.size() / ITEMS_PER_PAGE);
        int currentPage = (selectedIndex / ITEMS_PER_PAGE) + 1;
        String pageText = String.format("Page %d of %d (All: %d)", currentPage, totalPages, games.size());
        DrawText(pageText, PADDING, HEIGHT - 30, PARAGRAPH_SIZE, RAYWHITE);
    }
}