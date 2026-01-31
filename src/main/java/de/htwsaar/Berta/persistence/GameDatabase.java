package de.htwsaar.Berta.persistence;

import static de.htwsaar.Berta.db.Tables.GAMES;

import java.sql.*;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class GameDatabase implements DatabaseService {

    public Connection conn;
    public DSLContext dsl;

    public GameDatabase() throws SQLException {
        this.conn = DriverManager.getConnection("jdbc:sqlite:database.db");
        this.dsl = DSL.using(conn);
    }

    @Override
    public void saveGameToDatabase(GameDTO dto) {
        dsl.insertInto(GAMES)
                .set(GAMES.NAME, dto.name())
                .set(GAMES.STEAM_ID, dto.steamId())
                .set(GAMES.IMAGE_URL, dto.image_url())
                .set(GAMES.PRICE_CENTS, dto.price())
                .onConflict(GAMES.STEAM_ID)
                .doUpdate()
                .set(GAMES.NAME, dto.name())
                .execute();

    }

    @Override
    public void removeGameFromDatabase(GameDTO dto) {
        dsl.deleteFrom(GAMES)
                .where(GAMES.STEAM_ID.eq(dto.steamId()))
                .execute();
    }

    @Override
    public void close() throws SQLException {
        conn.close();
    }

    @Override
    public void SetupDatabase() {

    }

}
