package cz.pexeso.ui;

import cz.pexeso.domain.Score;
import cz.pexeso.persistence.ScoreRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class ScoresController implements Initializable {

    @FXML
    private TableView<Score> scoresTable;
    @FXML
    private TableColumn<Score, String> playerNameColumn;
    @FXML
    private TableColumn<Score, Integer> movesColumn;
    @FXML
    private TableColumn<Score, Integer> timeColumn;
    @FXML
    private TableColumn<Score, String> gridSizeColumn;
    @FXML
    private TableColumn<Score, LocalDateTime> dateColumn;

    private ScoreRepository scoreRepository;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.scoreRepository = new ScoreRepository();

        // Propojení sloupců s atributy třídy Score
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        movesColumn.setCellValueFactory(new PropertyValueFactory<>("moves"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("elapsedSeconds"));
        gridSizeColumn.setCellValueFactory(new PropertyValueFactory<>("gridSize"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        loadScores();
    }

    private void loadScores() {
        List<Score> scoreList = scoreRepository.findAll();
        ObservableList<Score> observableScoreList = FXCollections.observableArrayList(scoreList);
        scoresTable.setItems(observableScoreList);
    }
}
