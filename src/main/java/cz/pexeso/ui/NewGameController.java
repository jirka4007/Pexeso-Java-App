package cz.pexeso.ui;

import cz.pexeso.domain.Theme;
import cz.pexeso.service.ThemeService;
import cz.pexeso.service.ThemeServiceImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

public class NewGameController {

    @FXML
    private ComboBox<String> gridSizeComboBox;
    @FXML
    private ComboBox<Theme> themeComboBox;
    @FXML
    private TextField playerNameField;
    @FXML
    private Button startButton;

    private MainWindowController mainWindowController;
    private ThemeService themeService;

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    @FXML
    public void initialize() {
        this.themeService = new ThemeServiceImpl();

        List<String> gridSizes = Arrays.asList("4x4", "6x6");
        gridSizeComboBox.setItems(FXCollections.observableArrayList(gridSizes));
        gridSizeComboBox.getSelectionModel().selectFirst();

        List<Theme> themes = themeService.getAllThemes();
        if (themes.isEmpty()) {
            themeComboBox.setPromptText("Nejprve naimportujte téma");
            themeComboBox.setDisable(true);
            startButton.setDisable(true);
        } else {
            themeComboBox.setItems(FXCollections.observableArrayList(themes));
            themeComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void startGame() {
        Theme selectedTheme = themeComboBox.getSelectionModel().getSelectedItem();
        String selectedGridSize = gridSizeComboBox.getSelectionModel().getSelectedItem();
        String playerName = playerNameField.getText();
        mainWindowController.startNewGameWithSettings(selectedGridSize, selectedTheme, playerName);
    }

    @FXML
    private void goBack() {
        mainWindowController.showMainMenu();
    }
}
