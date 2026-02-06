# Berta Game Library

*Berta Game Library* ist eine Java-basierte Desktop-Anwendung zur Verwaltung einer persönlichen Spielesammlung. Die Anwendung ermöglicht es Benutzern, über die Steam-API nach Spielen zu suchen, Details einzusehen und diese in einer lokalen SQLite-Datenbank zu speichern.

Das Projekt nutzt eine moderne Architektur mit einer grafischen Oberfläche auf Basis von *Raylib (Jaylib)* und eine effiziente Datenbankanbindung via *jOOQ*.

---

## 🚀 Features

* *Steam-Integration:* Live-Suche nach Spielen über die offizielle Steam Store API.
* *Lokale Bibliothek:* Speichern von Favoriten in einer lokalen Datenbank inklusive Preis- und ID-Informationen.
* *CRUD-Operationen:* Einfaches Hinzufügen (Save) und Entfernen (Delete) von Spielen aus der Sammlung.
* *Moderne UI:* Leichtgewichtige, tastaturgesteuerte Benutzeroberfläche mit Raylib.
* *Cross-Platform Support:* Spezieller Bootstrap-Mechanismus für macOS-Kompatibilität (-XstartOnFirstThread).

---

## 🛠 Architektur & Technologien

Das Projekt folgt einer klaren Schichtentrennung (Layered Architecture):



1.  *Presentation Layer (de.htwsaar.Berta.presentation):*
   * Nutzt *Raylib* für das Rendering.
   * State-Management über die Screen-Klasse zur Steuerung von Navigation, Suche und Detailansichten.
2.  *Service Layer (de.htwsaar.Berta.servicelayer):*
   * Enthält die Geschäftslogik.
   * SteamIntegration kommuniziert via HttpClient mit der Steam-API und parst JSON-Daten mittels *Jackson*.
3.  *Persistence Layer (de.htwsaar.Berta.persistence):*
   * *SQLite* als leichtgewichtige Datenbank.
   * *jOOQ* für typsichere SQL-Abfragen und einfache Datenbankinteraktion.

---

## ⌨️ Bedienung (Controls)

Die Steuerung erfolgt vollständig über die Tastatur:

| Taste | Aktion |
| :--- | :--- |
| *[S]* | Suche öffnen / Suchmodus aktivieren |
| *[Pfeiltasten]* | Navigation durch die Spieleliste (Hoch/Runter) |
| *[ENTER]* | Details zum ausgewählten Spiel anzeigen / Suche bestätigen |
| *[C]* | Spiel in der Datenbank speichern (im Suchergebnis-Modus) |
| *[R]* | Spiel aus der Datenbank entfernen (in der Bibliotheks-Ansicht) |
| *[H]* | Zurück zur Startseite (Home / Lokale Bibliothek) |
| *[Pfeil UNTEN]* | Popups oder Detailansichten schließen |

---

## 📦 Installation & Start

### Voraussetzungen
* *Java 21* oder höher.
* *Maven* für das Dependency Management).
* Abhängigkeiten: Jaylib (Raylib), jOOQ, Jackson-Databind, SQLite-JDBC.

### Starten
Führe die Hauptklasse aus:
mvn compile
mvn exec:java
