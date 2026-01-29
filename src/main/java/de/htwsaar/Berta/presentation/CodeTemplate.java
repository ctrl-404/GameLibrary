package de.htwsaar.Berta.presentation;

import java.util.*;
import java.sql.*;
import com.raylib.Raylib;
import static com.raylib.Raylib.*;

/*
 * Simple Main-Methode als Überblick für eine JDBC Datenbanken implemenation
 * mit Jaylib GUI. Innerhalb einer Main-Methode der Einfacheits wegen.
 *
 * Jaylib: Schnittstelle zwischen Java und C basierten Raylib.
 */

public class CodeTemplate {
  // Stellt eine Game-Entity aus der Datenbank da.
  record Game(String name, String publisher) {}
  
  // Verbindungs-URL der Datenbank
  public static final String url="jdbc:sqlite:database.db";

  // Eigene GUI-Farbwerte
  public static final Raylib.Color myWhite = new Raylib.Color().r((byte)245).g((byte)245).b((byte)245).a((byte)255);
  public static final Raylib.Color myBlue = new Raylib.Color().r((byte)0).g((byte)121).b((byte)241).a((byte)255);
  public static final Raylib.Color myBlack = new Raylib.Color().r((byte)0).g((byte)0).b((byte)0).a((byte)100);
  public static final Raylib.Color myGray = new Raylib.Color().r((byte)150).g((byte)150).b((byte)150).a((byte)255);

  // GUI-Textpadding für einhaltliche Darstellung
  public static final int textPadding=20;

  public static void draw() {
    List<Game> gameList=new ArrayList<>();

    // Stellt Verbindung zur Datenbank her.
    try (var conn=DriverManager.getConnection(url)) {
      
      // Einlesen der Datenbankdaten und Einfügen derer in die Game-Liste
      String selectSql="SELECT gameName, publisherName FROM games";
      try (var stmt=conn.createStatement();
          var rs=stmt.executeQuery(selectSql)) {
        while (rs.next()) {
          gameList.add(new Game(rs.getString("gameName"), rs.getString("publisherName")));
        }
          }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }

    // GUI-Rendering
    InitWindow(800, 450, "Raylib Database Viz");
    SetTargetFPS(60);
    while (!WindowShouldClose()) {
      BeginDrawing();
      ClearBackground(myBlack);

      // Zeichnet unterstrichene Überschrift
      DrawText("DATABASE RECORDS:", textPadding, textPadding, textPadding, myWhite);
      DrawLine(textPadding, (2*textPadding), 300, (2*textPadding), myGray);

      // Zeichnet alle Game-Listelemente ins Fenster
      for (int i=0; i<gameList.size(); i++) {
        Game game=gameList.get(i);
        String displayString=String.format("%s - (%s)", game.name(), game.publisher());
        int yPos=(3*textPadding)+(i*textPadding); 
        DrawText(displayString, (2*textPadding), yPos, textPadding, myBlue);
      }
      EndDrawing();
    }
    CloseWindow();
  }
}
