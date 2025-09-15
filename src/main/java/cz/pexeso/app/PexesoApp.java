package cz.pexeso.app;

import cz.pexeso.infrastructure.DatabaseManager;
import cz.pexeso.ui.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;

import java.io.IOException;

public class PexesoApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Inicializace databáze při startu aplikace
        DatabaseManager.initializeDatabase();

        FXMLLoader fxmlLoader = new FXMLLoader(PexesoApp.class.getResource("/ui/MainWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        
        // Předání stage do controlleru
        MainWindowController controller = fxmlLoader.getController();
        controller.setStage(stage);
        
        // Načtení a aplikace CSS stylu
        String css = this.getClass().getResource("/ui/styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setTitle("Pexeso");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint(""); // Skryje hlášku "Press Esc to exit full screen"
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Deaktivuje Esc pro opuštění fullscreenu

        // Vynucení fullscreenu
        stage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal) {
                stage.setFullScreen(true);
            }
        });
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
