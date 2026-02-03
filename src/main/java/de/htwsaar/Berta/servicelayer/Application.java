package de.htwsaar.Berta.servicelayer;

import java.sql.SQLException;
import de.htwsaar.Berta.persistence.DatabaseService;
import de.htwsaar.Berta.persistence.GameDatabase;
import de.htwsaar.Berta.presentation.WindowManager;

public class Application {

  public final DatabaseService databaseService;
  public final GameService gameService;

  public Application() throws SQLException {
    databaseService = new GameDatabase();
    gameService = new SteamIntegration();
  }

  public void run() {
    try {
      WindowManager windowManager = new WindowManager(databaseService);
      windowManager.run();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
