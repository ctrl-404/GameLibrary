package de.htwsaar.Berta.persistence;

import static de.htwsaar.Berta.db.Tables.GAMES;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

/**
 * Implementierung des DatabaseService unter Verwendung von SQLite und jOOQ.
 */
public class GameDatabase implements DatabaseService {

    private final Connection conn;
    private final DSLContext dsl;
    private static final String DB_URL = "jdbc:sqlite:database.db";

    /**
     * Konstruktor: Stellt die Verbindung zur Datenbank her.
     *
     * @throws SQLException Wenn die Verbindung fehlschlägt.
     */
    public GameDatabase() throws SQLException {
        this.conn = DriverManager.getConnection(DB_URL);
        this.dsl = DSL.using(conn);
    }

    @Override
    public void saveGameToDatabase(GameDTO dto) {
        dsl.insertInto(GAMES)
                .set(GAMES.NAME, dto.name())
                .set(GAMES.STEAM_ID, dto.steamId())
                .set(GAMES.IMAGE_URL, dto.imageUrl())
                .set(GAMES.PRICE_CENTS, dto.price())
                .onConflict(GAMES.STEAM_ID)
                .doUpdate()
                .set(GAMES.NAME, dto.name())
                .set(GAMES.PRICE_CENTS, dto.price()) // Preis könnte sich ändern
                .execute();
    }

    @Override
    public void removeGameFromDatabase(GameDTO dto) {
        dsl.deleteFrom(GAMES)
                .where(GAMES.STEAM_ID.eq(dto.steamId()))
                .execute();
    }

    @Override
    public List<GameDTO> getAllGames() {
        return dsl.selectFrom(GAMES)
                .fetch(record -> new GameDTO(
                        record.get(GAMES.NAME),
                        record.get(GAMES.STEAM_ID),
                        record.get(GAMES.IMAGE_URL),
                        record.get(GAMES.PRICE_CENTS)));
    }

    @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}