package cz.pexeso.ui;

import cz.pexeso.domain.Karticka;
import cz.pexeso.service.GameService;
import cz.pexeso.service.GameServiceImpl;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Optional;

import cz.pexeso.domain.Score;
import cz.pexeso.persistence.ScoreRepository;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import cz.pexeso.domain.Theme;
import cz.pexeso.service.GameSetupException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import cz.pexeso.domain.GameState;
import cz.pexeso.service.GameStateService;
import cz.pexeso.service.GameStateServiceImpl;
import java.util.stream.Collectors;
import javafx.scene.layout.BorderPane;
import cz.pexeso.domain.PexesoImage;
import cz.pexeso.persistence.ThemeRepository;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Node;

public class GameBoardController implements Initializable {

    @FXML
    private Label tahyLabel;
    @FXML
    private Label casLabel;
    @FXML
    private GridPane hraciPlochaGrid;

    private GameServiceImpl gameService;
    private List<Karticka> karticky;
    private Map<Karticka, Button> mapaKaretNaTlacitka = new HashMap<>();
    private Button prvniOdkryteTlacitko = null;
    private int pocetTahu = 0;
    private Image zadniStranaKarty;
    private Timeline timer;
    private IntegerProperty elapsedSeconds = new SimpleIntegerProperty(0);
    private String selectedGridSize;
    private GameStateService gameStateService;
    private Theme currentTheme;
    private Stage stage;
    private int pocetSloupcu;
    private int pocetRadku;
    private double cardSize; // Nahrazení konstanty proměnnou
    private String playerName; // Pro uložení jména hráče na začátku hry
    private ScoreRepository scoreRepository;

    private MainWindowController mainWindowController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.gameService = new GameServiceImpl();
        this.gameStateService = new GameStateServiceImpl();
        this.scoreRepository = new ScoreRepository();
        // Načteme obrázek pro zadní stranu karty
        try {
            // Předpokládáme, že obrázek je v resources/images
            zadniStranaKarty = new Image(getClass().getResourceAsStream("/images/card_back.png"));
        } catch (Exception e) {
            System.err.println("Nepodařilo se načíst zadní stranu karty.");
            // Můžeme nastavit nějaký fallback nebo nechat null
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        // Posluchač již není potřeba, protože jsme vždy ve fullscreenu
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    private void updateAllCardSizes() {
        double newSize = this.cardSize;
        for (Button button : mapaKaretNaTlacitka.values()) {
            button.setPrefSize(newSize, newSize);
            button.setMinSize(newSize, newSize);
            button.setMaxSize(newSize, newSize);
            ImageView imageView = (ImageView) button.getGraphic();
            if (imageView != null) {
                imageView.setFitWidth(newSize);
                imageView.setFitHeight(newSize);
            }
        }
    }

    public void startNewGame(int sloupce, int radky, Theme theme, String playerName) {
        this.currentTheme = theme;
        this.pocetSloupcu = sloupce;
        this.pocetRadku = radky;
        this.selectedGridSize = sloupce + "x" + radky;
        this.playerName = (playerName != null && !playerName.trim().isEmpty()) ? playerName : null;

        // --- Adaptivní velikost karet a mezer ---
        if (sloupce == 6) {
            this.cardSize = 160.0;
            hraciPlochaGrid.setHgap(10);
            hraciPlochaGrid.setVgap(10);
        } else { // Výchozí pro 4x4
            this.cardSize = 240.0;
            hraciPlochaGrid.setHgap(15);
            hraciPlochaGrid.setVgap(15);
        }
        // -----------------------------------------

        try {
            int pocetParu = (sloupce * radky) / 2;
            karticky = gameService.vytvorNovouHru(pocetParu, theme);
            pocetTahu = 0;
            tahyLabel.setText("0");
            mapaKaretNaTlacitka.clear();

            hraciPlochaGrid.getChildren().clear();
            hraciPlochaGrid.getColumnConstraints().clear();
            hraciPlochaGrid.getRowConstraints().clear();

            setupGameBoardUI();
            startTimer();
        } catch (GameSetupException e) {
            // Zobrazit alert uživateli
            System.err.println(e.getMessage());
        }
    }

    private void setupGameBoardUI() {
        hraciPlochaGrid.getChildren().clear();
        hraciPlochaGrid.getColumnConstraints().clear();
        hraciPlochaGrid.getRowConstraints().clear();

        hraciPlochaGrid.setAlignment(Pos.CENTER);

        // Musíme znovu definovat sloupce a řádky, ale bez roztahování
        for (int i = 0; i < pocetSloupcu; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHgrow(Priority.NEVER); // Zabráníme roztahování
            hraciPlochaGrid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < pocetRadku; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setVgrow(Priority.NEVER); // Zabráníme roztahování
            hraciPlochaGrid.getRowConstraints().add(rowConst);
        }


        for (int i = 0; i < karticky.size(); i++) {
            Karticka karticka = karticky.get(i);
            Button tlacitkoKarty = createCardButton(karticka);
            mapaKaretNaTlacitka.put(karticka, tlacitkoKarty);

            // SPRÁVNÝ VÝPOČET
            int sloupec = i % pocetSloupcu;
            int radek = i / pocetSloupcu;
            
            hraciPlochaGrid.add(tlacitkoKarty, sloupec, radek);
        }
        updateAllCardSizes();
    }

    public void restoreGame(GameState gameState) {
        // Obnovit základní data
        this.selectedGridSize = gameState.getGridSize();
        this.pocetTahu = gameState.getMoves();
        this.elapsedSeconds.set(gameState.getElapsedSeconds());
        
        // Najít téma v databázi
        ThemeRepository themeRepository = new ThemeRepository();
        this.currentTheme = themeRepository.findAll().stream()
                .filter(t -> t.getId() == gameState.getThemeId())
                .findFirst().orElse(null);

        if (currentTheme == null) { /* handle error */ return; }

        // Zrekonstruovat seznam karet
        this.karticky = gameState.getCardStates().stream()
            .map(cs -> {
                PexesoImage img = currentTheme.getImages().stream()
                        .filter(i -> i.getId() == cs.getImageId()).findFirst().get();
                Karticka k = new Karticka(img);
                k.setStav(cs.getStatus());
                return k;
            }).collect(Collectors.toList());
        
        // Znovu vykreslit desku
        setupGameBoardUI();
        startTimer();
    }

    private Button createCardButton(Karticka karticka) {
        Button button = new Button();
        ImageView imageView = new ImageView(zadniStranaKarty);
        imageView.setPreserveRatio(true);

        button.setGraphic(imageView);

        // Tyto řádky se zdají být v konfliktu s `setPrefSize`, zkusíme je zakomentovat
        // GridPane.setHalignment(button, HPos.CENTER);
        // GridPane.setValignment(button, VPos.CENTER);
        button.setOnAction(event -> handleCardClick(karticka, button));

        return button;
    }

    private void handleCardClick(Karticka karticka, Button button) {
        GameService.TahResult vysledek = gameService.zpracujTah(karticka);

        if (vysledek == GameService.TahResult.NEPLATNY_TAH) {
            return; // Nic nedělat
        }
        
        Image predniStrana = new Image("file:" + karticka.getPexesoImage().getImagePath());
        ((ImageView) button.getGraphic()).setImage(predniStrana);

        switch (vysledek) {
            case PRVNI_KARTA_ODKRYTA:
                prvniOdkryteTlacitko = button;
                break;
            case SHODA:
                pocetTahu++;
                button.setDisable(true);
                if (prvniOdkryteTlacitko != null) {
                    prvniOdkryteTlacitko.setDisable(true);
                }
                prvniOdkryteTlacitko = null; // Reset pro další tah
                
                if (isGameOver()) {
                    handleGameOver();
                }
                break;
            case NESHODA:
                pocetTahu++;
                hraciPlochaGrid.setDisable(true);
                
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    ((ImageView) button.getGraphic()).setImage(zadniStranaKarty);
                    if (prvniOdkryteTlacitko != null) {
                        ((ImageView) prvniOdkryteTlacitko.getGraphic()).setImage(zadniStranaKarty);
                    }
                    hraciPlochaGrid.setDisable(false);
                    gameService.resetPrvniOdkrytouKartu();
                    prvniOdkryteTlacitko = null; // Reset pro další tah
                });
                pause.play();
                break;
        }
        tahyLabel.setText(String.valueOf(pocetTahu));
    }

    private boolean isGameOver() {
        return karticky.stream().allMatch(k -> k.getStav() == Karticka.StavKarty.SPAROVANA);
    }

    @FXML
    void saveAndExit(ActionEvent event) {
        stopTimer();
        
        GameState gameState = new GameState();
        gameState.setThemeId(currentTheme.getId());
        gameState.setGridSize(selectedGridSize);
        gameState.setMoves(pocetTahu);
        gameState.setElapsedSeconds(elapsedSeconds.get());
        
        List<GameState.CardState> cardStates = karticky.stream()
                .map(k -> new GameState.CardState(k.getPexesoImage().getId(), k.getStav()))
                .collect(Collectors.toList());
        gameState.setCardStates(cardStates);
        
        gameStateService.saveGame(gameState);
        
        // Návrat do hlavního menu (zjednodušeně)
        ((BorderPane) hraciPlochaGrid.getParent()).setCenter(new Label("Hra uložena. Vraťte se do menu."));
    }

    private void handleGameOver() {
        timer.stop();
        System.out.println("Konec hry!");

        if (playerName != null) {
            // Jméno bylo zadáno na začátku, uložíme skóre automaticky
            Score newScore = new Score();
            newScore.setPlayerName(playerName);
            newScore.setMoves(pocetTahu);
            newScore.setElapsedSeconds(elapsedSeconds.get());
            newScore.setGridSize(selectedGridSize);
            // createdAt se nastaví automaticky v konstruktoru nebo v repository
            
            scoreRepository.save(newScore);
            
            // Po uložení se vrátíme do hlavního menu
            mainWindowController.showMainMenu();
        } else {
            // Jméno nebylo zadáno, zobrazíme obrazovku pro uložení
            mainWindowController.showSaveScoreScreen(pocetTahu, elapsedSeconds.get(), selectedGridSize);
        }
    }
    
    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }
        elapsedSeconds.set(0);
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            elapsedSeconds.set(elapsedSeconds.get() + 1);
            int minutes = elapsedSeconds.get() / 60;
            int seconds = elapsedSeconds.get() % 60;
            casLabel.setText(String.format("%02d:%02d", minutes, seconds));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }
}
