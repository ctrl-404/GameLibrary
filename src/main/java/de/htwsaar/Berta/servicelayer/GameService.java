package de.htwsaar.Berta.servicelayer;

import java.util.ArrayList;

import de.htwsaar.Berta.persistence.GameDTO;

public interface GameService {
  public ArrayList<GameDTO> fetchGameList(String searchTerm);
}