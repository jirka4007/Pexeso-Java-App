package cz.pexeso.service;

import cz.pexeso.domain.GameState;
import java.util.Optional;

public interface GameStateService {

    /**
     * Uloží stav hry do souboru.
     * @param gameState Objekt reprezentující aktuální stav hry.
     */
    void saveGame(GameState gameState);

    /**
     * Načte stav hry ze souboru.
     * @return Optional s objektem GameState, pokud soubor existuje, jinak prázdný Optional.
     */
    Optional<GameState> loadGame();

    /**
     * Zkontroluje, zda existuje uložená hra.
     * @return true, pokud soubor s uloženou hrou existuje.
     */
    boolean gameStateExists();

    /**
     * Smaže soubor s uloženou hrou.
     */
    void deleteSavedGame();

}
