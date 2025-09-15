package cz.pexeso.domain;

import java.util.List;

public class GameState {

    private int themeId;
    private String gridSize;
    private int moves;
    private int elapsedSeconds;
    private List<CardState> cardStates;
    private int columns;
    private int rows;

    public GameState() {
        // Prázdný konstruktor pro Gson
    }

    // Gettery a Settery

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getGridSize() {
        return gridSize;
    }

    public void setGridSize(String gridSize) {
        this.gridSize = gridSize;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public List<CardState> getCardStates() {
        return cardStates;
    }

    public void setCardStates(List<CardState> cardStates) {
        this.cardStates = cardStates;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Vnořená statická třída pro reprezentaci stavu jedné karty.
     */
    public static class CardState {
        private int imageId;
        private Karticka.StavKarty status;

        public CardState() {
            // Prázdný konstruktor pro Gson
        }

        public CardState(int imageId, Karticka.StavKarty status) {
            this.imageId = imageId;
            this.status = status;
        }

        public int getImageId() {
            return imageId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public Karticka.StavKarty getStatus() {
            return status;
        }

        public void setStatus(Karticka.StavKarty status) {
            this.status = status;
        }
    }
}
