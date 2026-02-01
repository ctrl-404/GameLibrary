package de.htwsaar.Berta.servicelayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.Berta.persistence.GameDTO;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class SteamIntegration implements GameService {

  // private static final String DB_URL = "jdbc:sqlite:database.db";
  private static final String STEAM_SEARCH_URL = "https://store.steampowered.com/api/storesearch/?term=%s&l=english&cc=US";
  private final ObjectMapper mapper;
  private final HttpClient httpClient;

  public SteamIntegration() {
    this.mapper = new ObjectMapper();
    this.httpClient = HttpClient.newHttpClient();
  }

  public static String createUrl(String searchTerm) {
    String encodedSearch = java.net.URLEncoder.encode(searchTerm, java.nio.charset.StandardCharsets.UTF_8);
    String url = String.format(STEAM_SEARCH_URL, encodedSearch);
    return url;
  }

  public static HttpRequest buildHttpRequest(String url) {
    return HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
  }

  public JsonNode convertResponseToArray(HttpResponse<String> response) {
    try {
      JsonNode root = mapper.readTree(response.body());
      JsonNode items = root.path("items");
      return items;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }

  public HttpResponse<String> sendRequest(HttpRequest request) {
    HttpResponse<String> response;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.statusCode());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }

    return response;
  }

  public ArrayList<GameDTO> fetch(String searchTerm) {
    String url = createUrl(searchTerm);
    System.out.println("Requesting URL: " + url);

    HttpRequest request = buildHttpRequest(url);
    HttpResponse<String> response = sendRequest(request);

    JsonNode items = convertResponseToArray(response);

    ArrayList<GameDTO> dtoList = new ArrayList<>();

    if (items.isArray() && !items.isEmpty()) {
      for (JsonNode item : items) {
        int steamId = item.path("id").asInt();
        String name = item.path("name").asText();
        String image_url = item.path("tiny_image").asText();
        int price = item.path("price").path("final").asInt(0);
        GameDTO dto = new GameDTO(name, steamId, image_url, price);
        dtoList.add(dto);
        System.out.println("Found Game: " + name + " (ID: " + steamId + ")");
      }
    } else {
      System.out.println("Steam API returned 0 items. Try a broader search term like 'Valve'.");
    }
    return dtoList;
  }

  @Override
  public ArrayList<GameDTO> fetchGameList(String searchTerm) {
    return fetch(searchTerm);
  }
}
