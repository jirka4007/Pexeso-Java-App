package cz.pexeso.service;

import cz.pexeso.domain.Karticka;
import cz.pexeso.domain.PexesoImage;
import cz.pexeso.domain.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceImplTest {

    private GameServiceImpl gameService;
    private Theme testTheme;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl();
        // Vytvoření mockovacího tématu pro testování
        testTheme = new Theme(1, "Test Theme", "Popis");
        List<PexesoImage> images = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            images.add(new PexesoImage(i, 1, "/path/to/image" + i + ".png"));
        }
        testTheme.setImages(images);
    }

    @Test
    void testVytvorNovouHru_SpravnyPocetKaret() throws GameSetupException {
        int pocetParu = 8;
        List<Karticka> karticky = gameService.vytvorNovouHru(pocetParu, testTheme);
        assertEquals(pocetParu * 2, karticky.size(), "Musí být vygenerován dvojnásobek karet, než je počet párů.");
    }

    @Test
    void testVytvorNovouHru_NedostatekObrazku() {
        int pocetParu = 12; // Více než máme v testTheme
        assertThrows(GameSetupException.class, () -> {
            gameService.vytvorNovouHru(pocetParu, testTheme);
        }, "Měla by být vyhozena výjimka, pokud téma nemá dostatek obrázků.");
    }

    @Test
    void testZpracujTah_LogikaShodyANeshody() throws GameSetupException {
        // Vytvoříme hru s predictable kartami (nebudeme je míchat)
        List<Karticka> karticky = new ArrayList<>();
        PexesoImage img1 = new PexesoImage(1, 1, "path1");
        PexesoImage img2 = new PexesoImage(2, 1, "path2");
        karticky.add(new Karticka(img1));
        karticky.add(new Karticka(img2));
        karticky.add(new Karticka(img1)); // Druhá do páru

        // Test prvního tahu
        GameService.TahResult vysledek1 = gameService.zpracujTah(karticky.get(0));
        assertEquals(GameService.TahResult.PRVNI_KARTA_ODKRYTA, vysledek1);
        assertEquals(Karticka.StavKarty.ODKRYTA, karticky.get(0).getStav());

        // Test tahu se shodou
        GameService.TahResult vysledek2 = gameService.zpracujTah(karticky.get(2));
        assertEquals(GameService.TahResult.SHODA, vysledek2);
        assertEquals(Karticka.StavKarty.SPAROVANA, karticky.get(0).getStav());
        assertEquals(Karticka.StavKarty.SPAROVANA, karticky.get(2).getStav());

        // Test tahu s neshodou
        gameService.zpracujTah(karticky.get(0)); // První karta nového tahu
        GameService.TahResult vysledek3 = gameService.zpracujTah(karticky.get(1));
        assertEquals(GameService.TahResult.NESHODA, vysledek3);
        assertEquals(Karticka.StavKarty.ZAKRYTA, karticky.get(0).getStav());
        assertEquals(Karticka.StavKarty.ZAKRYTA, karticky.get(1).getStav());
    }
}
