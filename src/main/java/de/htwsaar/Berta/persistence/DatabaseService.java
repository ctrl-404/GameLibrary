package de.htwsaar.Berta.persistence;

import java.util.List;

public interface DatabaseService {

    public void saveGameToDatabase(GameDTO dto);

    public void close() throws Exception;

    public void SetupDatabase();

    public void removeGameFromDatabase(GameDTO dto);

    public List<GameDTO> getAllGames();
}

