package de.htwsaar.Berta.servicelayer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.Berta.persistence.GameDTO;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementierung des GameService zur Kommunikation mit der Steam Store API.
 */
public class SteamIntegration implements GameService {

  private static final String STEAM_SEARCH_URL_TEMPLATE = "https://store.steampowered.com/api/storesearch/?term=%s&l=english&cc=US";
  private final ObjectMapper mapper;
  private final HttpClient httpClient;

  public SteamIntegration() {
    this.mapper = new ObjectMapper();
    this.httpClient = HttpClient.newHttpClient();
  }

  @Override
  public List<GameDTO> fetchGameList(String searchTerm) {
    if (searchTerm == null || searchTerm.isBlank()) {
      return Collections.emptyList();
    }

    String url = createUrl(searchTerm);
    System.out.println("API request: " + url);

    try {
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
      HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() != 200) {
        System.err.println("API issue: Status Code " + response.statusCode());
        return Collections.emptyList();
      }

      return parseResponse(response.body());

    } catch (IOException | InterruptedException e) {
      System.err.println("Communication issue with Steam" + e.getMessage());
      return Collections.emptyList();
    }
  }

  private String createUrl(String searchTerm) {
    String encodedSearch = URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);
    return String.format(STEAM_SEARCH_URL_TEMPLATE, encodedSearch);
  }

  private List<GameDTO> parseResponse(String jsonBody) {
    List<GameDTO> dtoList = new ArrayList<>();
    try {
      JsonNode root = mapper.readTree(jsonBody);
      JsonNode items = root.path("items");

      if (items.isArray() && !items.isEmpty()) {
        for (JsonNode item : items) {
          dtoList.add(mapJsonToDTO(item));
        }
      } else {
        System.out.println("No Games found");
      }
    } catch (IOException e) {
      System.err.println("Parsing Issue with JSON response" + e.getMessage());
    }
    return dtoList;
  }

  private GameDTO mapJsonToDTO(JsonNode item) {
    int steamId = item.path("id").asInt();
    String name = String.valueOf(item.path("name"));
    String imageUrl = String.valueOf(item.path("tiny_image"));
    int price = item.path("price").path("final").asInt(0);

    System.out.printf("Game found: %s (ID: %d)%n", name, steamId);
    return new GameDTO(name, steamId, imageUrl, price);
  }
}