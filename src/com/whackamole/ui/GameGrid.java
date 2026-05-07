package com.whackamole.ui;

import com.whackamole.engine.GameEngine;
import com.whackamole.engine.HighScoreManager;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;


public class GameGrid extends JFrame {
    private final int rows;
    private final int cols;
    private final HoleButton[] holes;
    private final JLabel scoreLabel;
    private final JLabel timeLabel;
    private final JPanel gridPanel;
    private final JLabel highScoreLabel;
    private GameEngine engine;
    private HighScoreManager highScoreManager;

    private final ImageIcon holeIcon;

    public GameGrid(int rows, int cols, ImageIcon moleIcon, ImageIcon bombIcon, ImageIcon bonusIcon, ImageIcon holeIcon, HighScoreManager highScoreManager){

        super("Whack-A-Mole");
        this.rows = rows; this.cols = cols;
        this.holeIcon = holeIcon;
        this.highScoreManager = highScoreManager;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER));
        highScoreLabel = new JLabel("High Score: 0");
        int stored = highScoreManager.load();
        highScoreLabel.setText("High Score: " + stored);

        scoreLabel = new JLabel("Score: 0");
        timeLabel = new JLabel("Time: 0s");
        top.add(scoreLabel);
        top.add(Box.createHorizontalStrut(20));
        top.add(highScoreLabel);
        top.add(Box.createHorizontalStrut(20));
        top.add(timeLabel);
        add(top, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        holes = new HoleButton[rows * cols];
        for (int i = 0; i < holes.length; i++) {
            HoleButton btn = new HoleButton(i, holeIcon);
            final int idx = i;
            btn.addActionListener(e -> {
                if (engine != null) engine.handleClick(idx);
            });
            holes[i] = btn;
            gridPanel.add(btn);
        }
        add(gridPanel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton startBtn = new JButton("Start");
        JButton exitBtn = new JButton("Exit");
        bottom.add(startBtn);
        bottom.add(exitBtn);
        add(bottom, BorderLayout.SOUTH);

        startBtn.addActionListener(e -> startGame(moleIcon, bombIcon, bonusIcon));
        exitBtn.addActionListener(e -> {
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (engine != null) {
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public void startGame(ImageIcon moleIcon, ImageIcon bombIcon, ImageIcon bonusIcon) {
        engine = new GameEngine(this, rows, cols, 30, moleIcon, bombIcon, bonusIcon, holeIcon);
        Thread t = new Thread(engine, "GameEngineThread");
        t.start();
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                t.interrupt();
            }
        });
    }

    public void updateTime(int secondsLeft, int currentScore) {
        timeLabel.setText("Time: " + secondsLeft + "s");
        scoreLabel.setText("Score: " + currentScore);
    }

    public void showOccupant(int index, ImageIcon icon) {
        holes[index].setIcon(icon);
    }

    public void setHoleToEmpty(int index) {
        holes[index].setIcon(holeIcon);
    }
    private ImageIcon resizeIcon(ImageIcon icon, int w, int h) {
        if (icon == null) return null;
        Image img = icon.getImage();
        Image scaled = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void gameOver(int finalScore) {

        int oldHigh = highScoreManager.load();

        if (finalScore > oldHigh) {
            highScoreManager.save(finalScore);
            highScoreLabel.setText("High Score: " + finalScore);
        }

        JOptionPane.showMessageDialog(
                this,
                "Game Over!\nYour score: " + finalScore + "\nHigh Score: " + Math.max(finalScore, oldHigh),
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

}
