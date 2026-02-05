package de.htwsaar.Berta.persistence;

/**
 * Ein Datenübertragungsobjekt (DTO), das die Informationen eines Spiels repräsentiert.
 *
 * @param name     Der Name des Spiels.
 * @param steamId  Die eindeutige Steam-ID des Spiels.
 * @param imageUrl Die URL zum Vorschaubild des Spiels.
 * @param price    Der Preis des Spiels in Cent.
 */
public record GameDTO(String name, int steamId, String imageUrl, int price) {}
