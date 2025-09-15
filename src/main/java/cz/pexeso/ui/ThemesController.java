package cz.pexeso.ui;

import cz.pexeso.domain.Theme;
import cz.pexeso.service.ThemeImportException;
import cz.pexeso.service.ThemeService;
import cz.pexeso.service.ThemeServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ThemesController implements Initializable {

    @FXML
    private ListView<Theme> themesListView;
    @FXML
    private Button importButton;
    @FXML
    private Button deleteButton;

    private ThemeService themeService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.themeService = new ThemeServiceImpl();
        loadThemes();

        importButton.setOnAction(event -> handleImportTheme());
        deleteButton.setOnAction(event -> handleDeleteTheme());
    }

    private void loadThemes() {
        List<Theme> themeList = themeService.getAllThemes();
        ObservableList<Theme> observableThemeList = FXCollections.observableArrayList(themeList);
        themesListView.setItems(observableThemeList);
    }

    private void handleImportTheme() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Vyberte adresář s obrázky pro nové téma");
        File selectedDirectory = directoryChooser.showDialog(importButton.getScene().getWindow());

        if (selectedDirectory != null) {
            try {
                themeService.importThemeFromDirectory(selectedDirectory);
                showAlert(Alert.AlertType.INFORMATION, "Import úspěšný", "Téma '" + selectedDirectory.getName() + "' bylo úspěšně naimportováno.");
                loadThemes(); // Obnovit seznam
            } catch (ThemeImportException e) {
                showAlert(Alert.AlertType.ERROR, "Chyba importu", e.getMessage());
            }
        }
    }

    private void handleDeleteTheme() {
        Theme selectedTheme = themesListView.getSelectionModel().getSelectedItem();
        if (selectedTheme != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrzení smazání");
            alert.setHeaderText("Opravdu si přejete smazat téma '" + selectedTheme.getName() + "'?");
            alert.setContentText("Tato akce je nevratná.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                themeService.deleteTheme(selectedTheme);
                loadThemes(); // Obnovit seznam
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Žádný výběr", "Prosím, vyberte téma, které chcete smazat.");
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
