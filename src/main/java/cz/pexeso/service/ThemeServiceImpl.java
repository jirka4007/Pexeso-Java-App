package cz.pexeso.service;

import cz.pexeso.domain.PexesoImage;
import cz.pexeso.domain.Theme;
import cz.pexeso.persistence.ThemeRepository;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg");

    public ThemeServiceImpl() {
        this.themeRepository = new ThemeRepository();
    }

    @Override
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    @Override
    public void importThemeFromDirectory(File directory) throws ThemeImportException {
        if (directory == null || !directory.isDirectory()) {
            throw new ThemeImportException("Vybraná cesta není platný adresář.");
        }

        File[] imageFiles = directory.listFiles((dir, name) ->
                SUPPORTED_EXTENSIONS.stream().anyMatch(name.toLowerCase()::endsWith));

        if (imageFiles == null || imageFiles.length == 0) {
            throw new ThemeImportException("Adresář neobsahuje žádné podporované obrázky (png, jpg, jpeg).");
        }

        if (imageFiles.length % 2 != 0) {
            throw new ThemeImportException("Adresář musí obsahovat sudý počet obrázků.");
        }

        String themeName = directory.getName();
        Theme newTheme = new Theme(themeName, "Importováno z " + directory.getAbsolutePath());

        List<PexesoImage> pexesoImages = Arrays.stream(imageFiles)
                .map(file -> new PexesoImage(0, file.getAbsolutePath())) // themeId bude doplněno v repository
                .collect(Collectors.toList());

        newTheme.setImages(pexesoImages);
        themeRepository.save(newTheme);
    }

    @Override
    public void deleteTheme(Theme theme) {
        if (theme != null) {
            themeRepository.delete(theme.getId());
        }
    }
}
