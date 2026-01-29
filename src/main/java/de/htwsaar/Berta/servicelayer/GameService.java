package de.htwsaar.Berta.servicelayer;

import de.htwsaar.Berta.servicelayer.SteamAPI;
import de.htwsaar.Berta.db.tables.records.GamesRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static de.htwsaar.Berta.db.Tables.GAMES;

public class GameService {

  private final SteamAPI api;
  private final DSLContext dsl;

  public GameService(String dbUrl) {
    this.api = new SteamAPI();
    this.dsl = DSL.using(dbUrl);
  }

  public void searchAndCacheGame(String searchTerm) {
    var foundGames = api.searchGames(searchTerm);

    for (var game : foundGames) {
      dsl.insertInto(GAMES)
        .set(GAMES.STEAM_ID, game.steamId())
        .set(GAMES.NAME, game.name())
        .set(GAMES.IMAGE_URL, game.imageUrl())
        .set(GAMES.PRICE_CENTS, game.priceCents())
        .onDuplicateKeyIgnore()
        .execute();

      System.out.println("Saved: " + game.name());
    }
  }
}
