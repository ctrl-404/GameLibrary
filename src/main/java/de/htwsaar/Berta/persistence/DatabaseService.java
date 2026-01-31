package de.htwsaar.Berta.persistence;

public interface DatabaseService {

    public void saveGameToDatabase(GameDTO dto);

    public void removeGameFromDatabase(GameDTO dto);

    public void close() throws Exception;

    public void SetupDatabase();    
    
}

