package cz.pexeso.service;

import cz.pexeso.domain.Theme;
import java.io.File;
import java.util.List;

public interface ThemeService {

    /**
     * Načte všechna témata z databáze.
     * @return Seznam témat.
     */
    List<Theme> getAllThemes();

    /**
     * Naimportuje nové téma z vybraného adresáře.
     * Provede validaci souborů a uloží téma do databáze.
     *
     * @param directory Adresář s obrázky.
     * @throws ThemeImportException pokud se import nezdaří (např. špatný počet souborů).
     */
    void importThemeFromDirectory(File directory) throws ThemeImportException;

    /**
     * Smaže vybrané téma včetně jeho obrázků z databáze.
     *
     * @param theme Téma ke smazání.
     */
    void deleteTheme(Theme theme);

}
