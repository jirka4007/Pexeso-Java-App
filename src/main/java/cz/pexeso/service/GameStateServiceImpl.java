package cz.pexeso.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.pexeso.domain.GameState;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

public class GameStateServiceImpl implements GameStateService {

    private static final String SAVE_FILE_PATH = System.getProperty("user.home") + "/.pexeso/gamestate.json";
    private final Gson gson;

    public GameStateServiceImpl() {
        // Pretty printing pro lepší čitelnost souboru
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void saveGame(GameState gameState) {
        try (FileWriter writer = new FileWriter(SAVE_FILE_PATH)) {
            gson.toJson(gameState, writer);
        } catch (IOException e) {
            System.err.println("Error saving game state: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Optional<GameState> loadGame() {
        File saveFile = new File(SAVE_FILE_PATH);
        if (!saveFile.exists()) {
            return Optional.empty();
        }

        try (FileReader reader = new FileReader(saveFile)) {
            GameState gameState = gson.fromJson(reader, GameState.class);
            return Optional.ofNullable(gameState);
        } catch (IOException e) {
            System.err.println("Error loading game state: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean gameStateExists() {
        return new File(SAVE_FILE_PATH).exists();
    }

    @Override
    public void deleteSavedGame() {
        File saveFile = new File(SAVE_FILE_PATH);
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }
}
