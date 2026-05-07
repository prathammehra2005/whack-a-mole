package com.whackamole.ui;

import com.whackamole.engine.HighScoreException;
import com.whackamole.engine.HighScoreManager;
import com.whackamole.model.PlayerScore;
import java.io.File;
import java.util.List;
import javax.swing.*;

public class WhackAMoleApp {
    private static final File SCOREFILE = new File(System.getProperty("user.home"), ".whackamole_scores.dat");

    public static void main(String[] args) {
        ImageIcon mole = new ImageIcon(WhackAMoleApp.class.getResource("/resources/mole.png"));
        ImageIcon bomb = new ImageIcon(WhackAMoleApp.class.getResource("/resources/bomb.png"));
        ImageIcon bonus = new ImageIcon(WhackAMoleApp.class.getResource("/resources/bonus_mole.png"));
        ImageIcon hole = new ImageIcon(WhackAMoleApp.class.getResource("/resources/hole.png"));
        HighScoreManager hsm = new HighScoreManager(SCOREFILE);
        try {
            List<PlayerScore> scores = hsm.loadScores();
            System.out.println("Loaded high scores: " + scores);
        } catch (HighScoreException e) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Could not load high scores. Starting fresh.", "HighScore Load Error", JOptionPane.WARNING_MESSAGE));
        }

        ImageIcon moleIcon = mole != null ? mole : new ImageIcon();
        ImageIcon bombIcon = bomb != null ? bomb : new ImageIcon();
        ImageIcon bonusIcon = bonus != null ? bonus : new ImageIcon();
        ImageIcon holeIcon = hole != null ? hole : new ImageIcon();

        SwingUtilities.invokeLater(() -> {

            GameGrid grid = new GameGrid(4, 4, mole, bomb, bonus, hole, hsm);

            grid.setVisible(true);
        });
    }

    private static ImageIcon loadIcon(String path) {
        try {
            return new ImageIcon(path);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + path);
            return null;
        }
    }
}
