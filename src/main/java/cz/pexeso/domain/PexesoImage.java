package cz.pexeso.domain;

public class PexesoImage {
    private int id;
    private int themeId;
    private String imagePath;

    public PexesoImage(int themeId, String imagePath) {
        this.themeId = themeId;
        this.imagePath = imagePath;
    }

    public PexesoImage(int id, int themeId, String imagePath) {
        this.id = id;
        this.themeId = themeId;
        this.imagePath = imagePath;
    }

    // Gettery a Settery

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
