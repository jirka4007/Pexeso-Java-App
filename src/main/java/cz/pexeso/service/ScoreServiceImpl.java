package cz.pexeso.service;

import cz.pexeso.domain.Score;
import cz.pexeso.persistence.ScoreRepository;

import java.util.List;

public class ScoreServiceImpl implements ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreServiceImpl() {
        this.scoreRepository = new ScoreRepository();
    }

    @Override
    public void saveScore(Score score) {
        scoreRepository.save(score);
    }

    @Override
    public List<Score> getAllScores() {
        return scoreRepository.findAll();
    }
}
