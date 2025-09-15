package cz.pexeso.service;

import cz.pexeso.domain.Karticka;
import cz.pexeso.domain.Theme;

import java.util.List;

public interface GameService {

    enum TahResult {
        PRVNI_KARTA_ODKRYTA,
        SHODA,
        NESHODA,
        NEPLATNY_TAH
    }

    /**
     * Vytvoří novou hru a vygeneruje rozložení karet.
     *
     * @param pocetParu Počet párů karet ve hře.
     * @return Seznam zamíchaných karet.
     */
    List<Karticka> vytvorNovouHru(int pocetParu, Theme theme) throws GameSetupException;

    /**
     * Zpracuje tah hráče - odkrytí karty.
     *
     * @param odkrytaKarta Karta, kterou hráč odkryl.
     * @return Výsledek tahu.
     */
    TahResult zpracujTah(Karticka karticka);

    /**
     * Vrátí první odkrytou kartu v aktuálním tahu.
     * @return První odkrytá karta, nebo null.
     */
    Karticka getPrvniOdkrytaKarta();
    
    boolean isGameOver();
    
    void resetPrvniOdkrytouKartu();
}
