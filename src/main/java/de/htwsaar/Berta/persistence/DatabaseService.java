package de.htwsaar.Berta.persistence;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static de.htwsaar.Berta.db.Tables.GAMES;

public interface DatabaseService {

    public void safeGameToDatabase(GameDTO dto);

    public void close() throws Exception;

    public void SetupDatabase();

    void saveGameToDatabase(GameDTO dto);

    void removeGameFromDatabase(GameDTO dto);

    public List<GameDTO> getAllGames();
}

