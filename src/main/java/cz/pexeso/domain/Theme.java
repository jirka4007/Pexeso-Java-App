package cz.pexeso.domain;

import java.util.ArrayList;
import java.util.List;

public class Theme {
    private int id;
    private String name;
    private String description;
    private List<PexesoImage> images;

    public Theme(String name, String description) {
        this.name = name;
        this.description = description;
        this.images = new ArrayList<>();
    }

    public Theme(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.images = new ArrayList<>();
    }

    // Gettery a Settery

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PexesoImage> getImages() {
        return images;
    }

    public void setImages(List<PexesoImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return name; // Pro jednoduché zobrazení v UI prvcích
    }
}
