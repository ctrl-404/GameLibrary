package de.htwsaar.Berta.servicelayer;

import org.jooq.DSLContext;

import de.htwsaar.Berta.persistence.GameDTO;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class GameService {

  private final DSLContext dsl;
  private final HttpClient httpClient;
  private final SteamIntegration integration;

  public GameService(DSLContext dsl) {
    this.dsl = dsl;
    this.httpClient = HttpClient.newHttpClient();
    this.integration = new SteamIntegration(this);
  }

  public ArrayList<GameDTO> fetchFromSteamAPI(String searchTerm) {
    ArrayList<GameDTO> returnedItemList = integration.fetch(searchTerm);
    return returnedItemList;
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

}
