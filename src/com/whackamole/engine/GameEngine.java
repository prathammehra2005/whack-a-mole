package com.whackamole.engine;

import com.whackamole.model.*;
import com.whackamole.ui.GameGrid;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Controls game state on a background thread.
 * All UI updates are marshaled through SwingUtilities.invokeLater(...)
 */
public class GameEngine implements Runnable {
    private final GameGrid ui; // UI callback target
    private volatile boolean running = true;
    private final int gridSize;
    private final HoleOccupant[] occupants;
    private int score = 0;
    private int timeRemainingSeconds;
    private final int spawnProbabilityPercent; // chance to spawn each tick per free hole

    private final ImageIcon moleIcon, bombIcon, bonusIcon, holeIcon;

    public GameEngine(GameGrid ui, int rows, int cols, int initialTimeSeconds,
                      ImageIcon moleIcon, ImageIcon bombIcon, ImageIcon bonusIcon, ImageIcon holeIcon) {
        this.ui = ui;
        this.gridSize = rows * cols;
        this.occupants = new HoleOccupant[gridSize];
        this.timeRemainingSeconds = initialTimeSeconds;
        this.spawnProbabilityPercent = 20; // can tweak
        this.moleIcon = moleIcon; this.bombIcon = bombIcon; this.bonusIcon = bonusIcon; this.holeIcon = holeIcon;
    }

    public void stop() { running = false; }

    @Override
    public void run() {
        final int tickMillis = 1000; // 1 second per tick
        while (running && timeRemainingSeconds > 0) {
            long tickStart = System.currentTimeMillis();

            // decrement timer
            timeRemainingSeconds--;
            // tell UI to update time and score
            SwingUtilities.invokeLater(() -> ui.updateTime(timeRemainingSeconds, score));

            // tick occupants
            for (int i = 0; i < gridSize; i++) {
                HoleOccupant occ = occupants[i];
                if (occ != null && occ.isVisible()) {
                    occ.tick();
                    if (!occ.isVisible()) {
                        occupants[i] = null;
                        final int idx = i;
                        SwingUtilities.invokeLater(() -> ui.setHoleToEmpty(idx));
                    }
                }
            }

            // spawn logic: pick some free holes and randomly spawn
            for (int i = 0; i < gridSize; i++) {
                if (occupants[i] == null) {
                    int roll = ThreadLocalRandom.current().nextInt(100);
                    if (roll < spawnProbabilityPercent) {
                        // choose spawn type
                        int typeRoll = ThreadLocalRandom.current().nextInt(100);
                        HoleOccupant newOcc;
                        if (typeRoll < 70) { // mole 70%
                            newOcc = new Mole(3, moleIcon);
                        } else if (typeRoll < 90) { // bomb 20%
                            newOcc = new Bomb(3, bombIcon);
                        } else { // bonus 10%
                            newOcc = new BonusMole(3, bonusIcon, 5);
                        }
                        if (occupants[i] != null) {
                            // should not happen; protect invariants
                            throw new InvalidGameStateException("Spawning into occupied hole: " + i);
                        }
                        occupants[i] = newOcc;
                        final int idx = i;
                        SwingUtilities.invokeLater(() -> ui.showOccupant(idx, newOcc.getImageIcon()));
                    }
                }
            }

            // Sleep until next tick, handling interrupts
            long elapsed = System.currentTimeMillis() - tickStart;
            long sleep = tickMillis - elapsed;
            if (sleep < 0) sleep = 0;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                // Graceful shutdown requested
                running = false;
                break;
            }
        }

        // Game over: notify UI (must be on EDT)
        SwingUtilities.invokeLater(() -> ui.gameOver(score));
    }

    /**
     * Called by UI when a hole is clicked. This method runs on EDT.
     */
    public synchronized void handleClick(int holeIndex) {
        if (holeIndex < 0 || holeIndex >= gridSize) throw new IllegalArgumentException("bad index");
        HoleOccupant occ = occupants[holeIndex];
        if (occ == null) {
            // clicking empty hole -> no effect
            return;
        }
        int delta = occ.whack();
        // apply side-effects for BonusMole
        if (occ instanceof BonusMole) {
            timeRemainingSeconds += ((BonusMole) occ).getExtraSeconds();
        }
        occupants[holeIndex] = null;
        // reflect in UI
        SwingUtilities.invokeLater(() -> {
            ui.setHoleToEmpty(holeIndex);
            // update score/time shown
            ui.updateTime(timeRemainingSeconds, score + delta);
        });
        score += delta;
    }

    public int getScore() { return score; }
}
