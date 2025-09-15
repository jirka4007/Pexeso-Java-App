package cz.pexeso.service;

import cz.pexeso.domain.Karticka;
import cz.pexeso.domain.Theme;
import cz.pexeso.domain.PexesoImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameServiceImpl implements GameService {

    private Karticka prvniOdkrytaKarta = null;
    private int pocetTahu = 0;
    private int pocetSpravneUhodnutychParu = 0;
    private int pocetParuCelkem = 0;

    @Override
    public List<Karticka> vytvorNovouHru(int pocetParu, Theme theme) throws GameSetupException {
        if (theme == null || theme.getImages() == null || theme.getImages().size() < pocetParu) {
            throw new GameSetupException("Vybrané téma neobsahuje dostatek obrázků (" + pocetParu + " párů).");
        }
        
        this.pocetParuCelkem = pocetParu; // Uložíme si celkový počet párů

        // Reset stavu pro novou hru
        prvniOdkrytaKarta = null;
        pocetTahu = 0;
        pocetSpravneUhodnutychParu = 0;

        List<PexesoImage> obrazkyProHru = new ArrayList<>(theme.getImages());
        Collections.shuffle(obrazkyProHru);
        obrazkyProHru = obrazkyProHru.subList(0, pocetParu);

        List<Karticka> karticky = new ArrayList<>();
        for (PexesoImage img : obrazkyProHru) {
            karticky.add(new Karticka(img));
            karticky.add(new Karticka(img));
        }

        Collections.shuffle(karticky);
        return karticky;
    }

    @Override
    public TahResult zpracujTah(Karticka odkrytaKarta) {
        if (odkrytaKarta.getStav() == Karticka.StavKarty.SPAROVANA || odkrytaKarta.getStav() == Karticka.StavKarty.ODKRYTA) {
            return TahResult.NEPLATNY_TAH;
        }

        odkrytaKarta.setStav(Karticka.StavKarty.ODKRYTA);

        if (prvniOdkrytaKarta == null) {
            // Toto je první karta v tahu
            prvniOdkrytaKarta = odkrytaKarta;
            return TahResult.PRVNI_KARTA_ODKRYTA;
        } else {
            // Toto je druhá karta, porovnáváme
            pocetTahu++;
            if (prvniOdkrytaKarta.getIdObrazku() == odkrytaKarta.getIdObrazku()) {
                // Shoda
                prvniOdkrytaKarta.setStav(Karticka.StavKarty.SPAROVANA);
                odkrytaKarta.setStav(Karticka.StavKarty.SPAROVANA);
                prvniOdkrytaKarta = null; // Reset pro další tah
                pocetSpravneUhodnutychParu++;
                return TahResult.SHODA;
            } else {
                // Neshoda
                prvniOdkrytaKarta.setStav(Karticka.StavKarty.ZAKRYTA);
                odkrytaKarta.setStav(Karticka.StavKarty.ZAKRYTA);
                // prvniOdkrytaKarta se resetuje až po vizuálním efektu v controlleru
                return TahResult.NESHODA;
            }
        }
    }

    @Override
    public boolean isGameOver() {
        return pocetSpravneUhodnutychParu == pocetParuCelkem;
    }

    @Override
    public Karticka getPrvniOdkrytaKarta() {
        return prvniOdkrytaKarta;
    }

    // Metodu pro resetování první karty budeme volat z controlleru po animaci neshody
    @Override
    public void resetPrvniOdkrytouKartu() {
        this.prvniOdkrytaKarta = null;
    }
}
