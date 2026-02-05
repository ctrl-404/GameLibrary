package de.htwsaar.Berta.servicelayer;

import de.htwsaar.Berta.persistence.GameDTO;
import java.util.List;

/**
 * Schnittstelle für Dienste, die Spieldaten abrufen (z.B. externe APIs).
 */
public interface GameService {

  /**
   * Sucht nach Spielen basierend auf einem Suchbegriff.
   *
   * @param searchTerm Der Suchbegriff.
   * @return Eine Liste von gefundenen Spielen (DTOs).
   */
  List<GameDTO> fetchGameList(String searchTerm);
}