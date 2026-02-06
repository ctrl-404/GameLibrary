# Berta Game Library

**GameLibrary** ist eine Anwendung welche eine Datenbank mit einer Steam-Api verbindet um Spiele auf Steam zu suchen und in der eigenen Datenbank zu speichern. Es ermöglicht Nutzer:innen eine persönliche Übersicht zu erstellen und zu verwalten.

---

## 1. Projektübersicht

## 2. Features

* **Steam API Integration:** Echtzeitsuche von Spielen innerhalb von Steam.
* **Lokales Library Management:** Das Speichern von Spielen sowie das Entfernen derer aus der eigenen Datenbank.
* **Retro UI:** GUI mit Hilfe von Raylib.
* **Keyboard Driven:** Alle Funktionen sind ohne Maus ausführbar.
* **Data Persistence:** Benutzt SQLite um Daten sicherzustellen.

---

## 3. Tech Stack

| Component        | Technology                                                |
|:-----------------|:----------------------------------------------------------|
| **Language**     | Java 21                                                   |
| **UI Framework** | [Raylib-j](https://github.com/electronstudio/raylib-java) |
| **JSON Parsing** | Jackson Databind                                          |
| **Networking**   | Java HTTP Client (Native)                                 |
| **Database**     | JDBC / SQLite                                             |

---

## 4. Controls

### Navigation
* **`UP / DOWN`**: Navigation durch die Spielelisten.
* **`ENTER`**: Anzeigen von Spieledetails / Bestätigung der Suche.
* **`H`**: Gehe zurück zur eigenen Datenbank.
* **`S`**: Öffnen der Suchleiste.
* **`ESC`**: Beendet die Anwendung.

### Innerhalb der Detail-Ansicht
* **`C`**: **Speichert** das ausgewählte Spiel in der Datenbank.
* **`R`**: **Entfernt** das ausgewählte Spiel aus der Datenbank.
* **`DOWN`**: Schließt die Detail-Ansicht.

---

## 5. Projektstruktur

* **`de.htwsaar.Berta.presentation`**: Handles the Raylib UI logic (`RaylibUserInterface`) and screen-state management (`Screen`).
* **`de.htwsaar.Berta.servicelayer`**: Contains the core logic (`Application`) and the Steam API integration (`SteamIntegration`).
* **`de.htwsaar.Berta.persistence`**: Manages the database connection (`GameDatabase`), setup, and Data Transfer Objects (`GameDTO`).

---

## 📝 License
This project was developed for educational purposes at **htwsaar**.
