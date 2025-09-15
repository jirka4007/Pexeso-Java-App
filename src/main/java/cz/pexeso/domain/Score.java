package cz.pexeso.domain;

import java.time.LocalDateTime;

public class Score {

    private int id;
    private String playerName;
    private int moves;
    private int elapsedSeconds;
    private String gridSize;
    private LocalDateTime createdAt;

    public Score() {
        // Prázdný konstruktor
    }

    public Score(String playerName, int moves, int elapsedSeconds, String gridSize) {
        this.playerName = playerName;
        this.moves = moves;
        this.elapsedSeconds = elapsedSeconds;
        this.gridSize = gridSize;
    }

    // Přetížený konstruktor pro načítání z DB, kde máme i ID a čas
    public Score(int id, String playerName, int moves, int elapsedSeconds, String gridSize, LocalDateTime createdAt) {
        this.id = id;
        this.playerName = playerName;
        this.moves = moves;
        this.elapsedSeconds = elapsedSeconds;
        this.gridSize = gridSize;
        this.createdAt = createdAt;
    }

    // Gettery a Settery

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public String getGridSize() {
        return gridSize;
    }

    public void setGridSize(String gridSize) {
        this.gridSize = gridSize;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
