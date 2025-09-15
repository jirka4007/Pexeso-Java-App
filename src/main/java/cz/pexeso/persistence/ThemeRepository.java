package cz.pexeso.persistence;

import cz.pexeso.domain.PexesoImage;
import cz.pexeso.domain.Theme;
import cz.pexeso.infrastructure.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ThemeRepository {

    public void save(Theme theme) {
        String themeSql = "INSERT INTO THEMES(name, description) VALUES(?,?)";
        String imageSql = "INSERT INTO IMAGES(theme_id, image_path) VALUES(?,?)";
        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);

            // Uložit téma a získat jeho ID
            PreparedStatement themePstmt = conn.prepareStatement(themeSql, Statement.RETURN_GENERATED_KEYS);
            themePstmt.setString(1, theme.getName());
            themePstmt.setString(2, theme.getDescription());
            themePstmt.executeUpdate();

            ResultSet rs = themePstmt.getGeneratedKeys();
            int themeId = -1;
            if (rs.next()) {
                themeId = rs.getInt(1);
            } else {
                throw new SQLException("Creating theme failed, no ID obtained.");
            }

            // Uložit všechny obrázky s ID tématu
            PreparedStatement imagePstmt = conn.prepareStatement(imageSql);
            for (PexesoImage image : theme.getImages()) {
                imagePstmt.setInt(1, themeId);
                imagePstmt.setString(2, image.getImagePath());
                imagePstmt.addBatch();
            }
            imagePstmt.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Transaction failed: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Theme> findAll() {
        String sql = "SELECT id, name, description FROM THEMES";
        List<Theme> themes = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Theme theme = new Theme(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
                theme.setImages(findImagesByThemeId(theme.getId()));
                themes.add(theme);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return themes;
    }

    public List<PexesoImage> findImagesByThemeId(int themeId) {
        String sql = "SELECT id, theme_id, image_path FROM IMAGES WHERE theme_id = ?";
        List<PexesoImage> images = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, themeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                images.add(new PexesoImage(rs.getInt("id"), rs.getInt("theme_id"), rs.getString("image_path")));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return images;
    }
    
    public void delete(int themeId) {
        String deleteImagesSql = "DELETE FROM IMAGES WHERE theme_id = ?";
        String deleteThemeSql = "DELETE FROM THEMES WHERE id = ?";
        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);
            
            // Smazat obrázky
            PreparedStatement pstmtImages = conn.prepareStatement(deleteImagesSql);
            pstmtImages.setInt(1, themeId);
            pstmtImages.executeUpdate();

            // Smazat téma
            PreparedStatement pstmtTheme = conn.prepareStatement(deleteThemeSql);
            pstmtTheme.setInt(1, themeId);
            pstmtTheme.executeUpdate();
            
            conn.commit();
        } catch (SQLException e) {
            // ... (error handling with rollback) ...
        } finally {
            // ... (close connection) ...
        }
    }
}
