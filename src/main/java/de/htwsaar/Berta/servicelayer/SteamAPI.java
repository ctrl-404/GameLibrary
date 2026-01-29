package de.htwsaar.Berta.servicelayer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class SteamAPI {
  private static final String SEARCH_URL = "https://store.steampowered.com/api/storesearch/?term=%s&l=english&cc=US";

  private final HttpClient client;
  private final ObjectMapper mapper;

  public SteamAPI() {
    this.client = HttpClient.newHttpClient();
    this.mapper = new ObjectMapper();
  }

  public record SteamGameDto(int steamId, String name, String imageUrl, int priceCents) {}

  public List<SteamGameDto> searchGames(String query) {
    List<SteamGameDto> results = new ArrayList<>();
    try {

      String formattedUrl = String.format(SEARCH_URL, query.replace(" ", "+"));
      HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(formattedUrl))
        .GET()
        .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      JsonNode root = mapper.readTree(response.body());
      JsonNode items = root.path("items");

      if (items.isArray()) {
        for (JsonNode item : items) {
          int id = item.path("id").asInt();
          String name = item.path("name").asText();
          String img = item.path("tiny_image").asText();

          int price = 0;
          if (item.has("price")) {
            price = item.path("price").path("final").asInt();
          }

          results.add(new SteamGameDto(id, name, img, price));
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return results;
  }
}
