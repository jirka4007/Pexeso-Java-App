package cz.pexeso.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:" + System.getProperty("user.home") + "/.pexeso/pexeso.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void initializeDatabase() {
        // Ujistíme se, že existuje adresář pro databázi
        new java.io.File(System.getProperty("user.home"), ".pexeso").mkdirs();

        String createScoresTable = "CREATE TABLE IF NOT EXISTS SCORES (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " player_name TEXT NOT NULL,\n"
                + " moves INTEGER NOT NULL,\n"
                + " elapsed_seconds INTEGER NOT NULL,\n"
                + " grid_size TEXT NOT NULL,\n" // např. "4x4"
                + " created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        String createThemesTable = "CREATE TABLE IF NOT EXISTS THEMES (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL UNIQUE,\n"
                + " description TEXT\n"
                + ");";

        String createImagesTable = "CREATE TABLE IF NOT EXISTS IMAGES (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " theme_id INTEGER NOT NULL,\n"
                + " image_path TEXT NOT NULL UNIQUE,\n"
                + " FOREIGN KEY (theme_id) REFERENCES THEMES (id)\n"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createScoresTable);
            stmt.execute(createThemesTable);
            stmt.execute(createImagesTable);
            System.out.println("Database schema initialized successfully.");
        } catch (SQLException e) {
            System.err.println("Error initializing database schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
