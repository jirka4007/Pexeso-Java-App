package cz.pexeso.service;

import cz.pexeso.domain.Score;

import java.util.List;

public interface ScoreService {
    void saveScore(Score score);
    List<Score> getAllScores();
}
