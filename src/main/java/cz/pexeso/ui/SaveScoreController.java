package cz.pexeso.ui;

import cz.pexeso.domain.Score;
import cz.pexeso.service.ScoreService;
import cz.pexeso.service.ScoreServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

public class SaveScoreController {

    @FXML
    private Label tahyLabel;
    @FXML
    private Label casLabel;
    @FXML
    private TextField jmenoTextField;

    private MainWindowController mainWindowController;
    private ScoreService scoreService;

    private int finalMoves;
    private int finalSeconds;
    private String gridSize;

    public void initialize() {
        this.scoreService = new ScoreServiceImpl();
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void setScoreData(int moves, int seconds, String gridSize) {
        this.finalMoves = moves;
        this.finalSeconds = seconds;
        this.gridSize = gridSize;
        tahyLabel.setText("Počet tahů: " + finalMoves);

        long minutes = finalSeconds / 60;
        long remainingSeconds = finalSeconds % 60;
        casLabel.setText(String.format("Čas: %02d:%02d", minutes, remainingSeconds));
    }

    @FXML
    void handleSave(ActionEvent event) {
        String playerName = jmenoTextField.getText();
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Anonymní hráč";
        }

        Score score = new Score();
        score.setPlayerName(playerName);
        score.setMoves(finalMoves);
        score.setElapsedSeconds(finalSeconds); // Opravený název metody
        score.setGridSize(this.gridSize);
        score.setCreatedAt(LocalDateTime.now()); // Opravený název a typ

        scoreService.saveScore(score);

        mainWindowController.showMainMenu();
    }

    @FXML
    void handleBack(ActionEvent event) {
        mainWindowController.showMainMenu();
    }
}
