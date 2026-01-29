package de.htwsaar.Berta.servicelayer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.Berta.db.tables.records.GamesRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;

import static de.htwsaar.Berta.db.Tables.GAMES;

public class SteamIntegration {

  private static final String DB_URL = "jdbc:sqlite:database.db";
  private static final String STEAM_SEARCH_URL = "https://store.steampowered.com/api/storesearch/?term=%s&l=english&cc=US";

  public static void main(String[] args) {
    fetchAndStoreGames("Cyberpunk");
  }

  public static void fetchAndStoreGames(String searchTerm) {
    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper mapper = new ObjectMapper();

    try {
      String encodedSearch = java.net.URLEncoder.encode(searchTerm, java.nio.charset.StandardCharsets.UTF_8);
      String url = String.format(STEAM_SEARCH_URL, encodedSearch);

      System.out.println("Requesting URL: " + url);

      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      System.out.println("Raw JSON Response: " + response.body());

      JsonNode root = mapper.readTree(response.body());
      JsonNode items = root.path("items");

      if (items.isArray() && !items.isEmpty()) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
          System.out.println("Writing to DB at: " + new java.io.File("database.db").getAbsolutePath());

          DSLContext dsl = DSL.using(conn);

          for (JsonNode item : items) {
            int steamId = item.path("id").asInt();
            String name = item.path("name").asText();

            System.out.println("Found Game: " + name + " (ID: " + steamId + ")");

            dsl.insertInto(GAMES)
              .set(GAMES.STEAM_ID, steamId)
              .set(GAMES.NAME, name)
              .set(GAMES.IMAGE_URL, item.path("tiny_image").asText())
              .set(GAMES.PRICE_CENTS, item.path("price").path("final").asInt(0))
              .onConflict(GAMES.STEAM_ID)
              .doUpdate()
              .set(GAMES.NAME, name)
              .execute();
          }
          System.out.println("Database sync complete.");
        }
      } else {
        System.out.println("Steam API returned 0 items. Try a broader search term like 'Valve'.");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
