package cz.pexeso.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import cz.pexeso.domain.Theme;
import cz.pexeso.domain.GameState;
import cz.pexeso.service.GameStateService;
import cz.pexeso.service.GameStateServiceImpl;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class MainWindowController implements Initializable {

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button continueButton;

    private GameStateService gameStateService;
    private Stage stage;
    private Node mainMenu; // Uložíme si hlavní menu

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gameStateService = new GameStateServiceImpl();
        continueButton.setDisable(!gameStateService.gameStateExists());
        mainMenu = rootPane.getCenter(); // Uložíme si původní obsah
        System.out.println("MainWindowController byl inicializován.");
    }

    @FXML
    void showGameBoard(ActionEvent event) {
        // Místo dialogu jen načteme nové view
        loadViewAndPassReference("NewGameView.fxml");
    }

    public void startNewGameWithSettings(String selectedGridSize, Theme selectedTheme, String playerName) {
        try {
            if (selectedTheme != null && selectedGridSize != null) {
                FXMLLoader gameBoardLoader = new FXMLLoader(getClass().getResource("/ui/GameBoardView.fxml"));
                Node gameBoardView = gameBoardLoader.load();
                GameBoardController gameBoardController = gameBoardLoader.getController();

                String[] dimensions = selectedGridSize.split("x");
                int cols = Integer.parseInt(dimensions[0]);
                int rows = Integer.parseInt(dimensions[1]);

                gameBoardController.setStage(stage);
                gameBoardController.setMainWindowController(this); // Důležité pro návrat a navigaci
                gameBoardController.startNewGame(cols, rows, selectedTheme, playerName);
                rootPane.setCenter(gameBoardView);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSaveScoreScreen(int moves, int seconds, String gridSize) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/SaveScoreView.fxml"));
            Parent saveScoreView = loader.load();
            rootPane.setCenter(saveScoreView);

            SaveScoreController controller = loader.getController();
            controller.setMainWindowController(this);
            controller.setScoreData(moves, seconds, gridSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMainMenu() {
        if (mainMenu != null) {
            rootPane.setCenter(mainMenu);
        }
        // Po návratu do menu znovu zkontrolujeme, zda existuje uložená hra
        if (gameStateService != null) {
            continueButton.setDisable(!gameStateService.gameStateExists());
        }
    }

    @FXML
    void continueGame(ActionEvent event) {
        gameStateService.loadGame().ifPresent(gameState -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/GameBoardView.fxml"));
                Parent gameBoardView = loader.load();
                rootPane.setCenter(gameBoardView);

                GameBoardController controller = loader.getController();
                controller.setStage(stage);
                controller.setMainWindowController(this); // Předání reference na sebe sama
                controller.restoreGame(gameState); // Předání načteného stavu hry
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void showScores(ActionEvent event) {
        loadViewAndPassReference("ScoresView.fxml");
    }

    @FXML
    void showThemes(ActionEvent event) {
        loadViewAndPassReference("ThemesView.fxml");
    }
    
    @FXML
    void exitGame(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void loadView(String fxmlFile) {
        try {
            URL fxmlUrl = getClass().getResource("/ui/" + fxmlFile);
            if (fxmlUrl == null) {
                System.err.println("Cannot find FXML file: " + fxmlFile);
                return;
            }
            Parent view = FXMLLoader.load(fxmlUrl);
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadViewAndPassReference(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/" + fxmlFile));
            Node view = loader.load();
            
            // Speciální případ pro NewGameController, kterému předáme referenci
            if (loader.getController() instanceof NewGameController) {
                NewGameController controller = loader.getController();
                controller.setMainWindowController(this);
            }
            
            rootPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
