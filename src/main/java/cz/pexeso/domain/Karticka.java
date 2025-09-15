package cz.pexeso.domain;

public class Karticka {

    private PexesoImage pexesoImage;
    private StavKarty stav;

    public enum StavKarty {
        ZAKRYTA,
        ODKRYTA,
        SPAROVANA
    }

    public Karticka(PexesoImage pexesoImage) {
        this.pexesoImage = pexesoImage;
        this.stav = StavKarty.ZAKRYTA;
    }

    public PexesoImage getPexesoImage() {
        return pexesoImage;
    }

    public int getIdObrazku() {
        return pexesoImage.getId();
    }

    public StavKarty getStav() {
        return stav;
    }

    public void setStav(StavKarty stav) {
        this.stav = stav;
    }

    @Override
    public String toString() {
        return "Karticka{" +
                "pexesoImage=" + pexesoImage.getId() +
                ", stav=" + stav +
                '}';
    }
}
