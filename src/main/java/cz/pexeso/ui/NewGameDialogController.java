package cz.pexeso.ui;

import cz.pexeso.domain.Theme;
import cz.pexeso.service.ThemeService;
import cz.pexeso.service.ThemeServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import java.util.Arrays;
import java.util.List;

public class NewGameDialogController {

    @FXML
    private ComboBox<String> gridSizeComboBox;
    @FXML
    private ComboBox<Theme> themeComboBox;

    private ThemeService themeService;
    private List<Theme> themes;

    @FXML
    public void initialize() {
        this.themeService = new ThemeServiceImpl();

        List<String> gridSizes = Arrays.asList("4x4", "6x6");
        gridSizeComboBox.setItems(FXCollections.observableArrayList(gridSizes));
        gridSizeComboBox.getSelectionModel().selectFirst();

        this.themes = themeService.getAllThemes();
        if (themes.isEmpty()) {
            themeComboBox.setPromptText("Žádná témata k dispozici");
            themeComboBox.setDisable(true);
        } else {
            themeComboBox.setItems(FXCollections.observableArrayList(themes));
            themeComboBox.getSelectionModel().selectFirst();
        }
    }

    public boolean hasThemes() {
        return themes != null && !themes.isEmpty();
    }

    public String getSelectedGridSize() {
        return gridSizeComboBox.getSelectionModel().getSelectedItem();
    }

    public Theme getSelectedTheme() {
        return themeComboBox.getSelectionModel().getSelectedItem();
    }
}
