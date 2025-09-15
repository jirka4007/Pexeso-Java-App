package cz.pexeso.persistence;

import cz.pexeso.domain.Score;
import cz.pexeso.infrastructure.DatabaseManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScoreRepository {

    public void save(Score score) {
        String sql = "INSERT INTO SCORES(player_name, moves, elapsed_seconds, grid_size) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, score.getPlayerName());
            pstmt.setInt(2, score.getMoves());
            pstmt.setInt(3, score.getElapsedSeconds());
            pstmt.setString(4, score.getGridSize());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Score> findAll() {
        String sql = "SELECT id, player_name, moves, elapsed_seconds, grid_size, created_at FROM SCORES ORDER BY moves ASC, elapsed_seconds ASC";
        List<Score> scores = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Score score = new Score(
                        rs.getInt("id"),
                        rs.getString("player_name"),
                        rs.getInt("moves"),
                        rs.getInt("elapsed_seconds"),
                        rs.getString("grid_size"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                scores.add(score);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return scores;
    }
}
