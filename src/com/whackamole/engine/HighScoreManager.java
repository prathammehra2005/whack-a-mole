package com.whackamole.engine;

import com.whackamole.model.PlayerScore;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HighScoreManager {
    private final File file;

    public HighScoreManager(File file) {
        this.file = file;
    }

    public synchronized void saveScores(List<PlayerScore> scores) throws HighScoreException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(scores));
        } catch (IOException e) {
            throw new HighScoreException("Failed to save scores", e);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized List<PlayerScore> loadScores() throws HighScoreException {
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<PlayerScore>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new HighScoreException("Failed to load scores", e);
        }
    }

    public int load() {
        try {
            List<PlayerScore> scores = loadScores();
            if (scores.isEmpty()) return 0;
            return scores.stream().mapToInt(PlayerScore::getScore).max().orElse(0);
        } catch (HighScoreException e) {
            return 0;
        }
    }

    public void save(int score) {
        try {
            List<PlayerScore> scores = loadScores();
            scores.add(new PlayerScore("Player", score));
            saveScores(scores);
        } catch (HighScoreException e) {
            e.printStackTrace();
        }
    }
}

