package com.whackamole.model;

import javax.swing.ImageIcon;
import java.io.Serializable;

public abstract class HoleOccupant implements Serializable {
    private static final long serialVersionUID = 1L;

    protected boolean visible = true;
    protected int timeRemaining; // ticks left

    public HoleOccupant(int lifeTicks) {
        this.timeRemaining = lifeTicks;
    }

    public boolean isVisible() {
        return visible;
    }

    public void hide() {
        visible = false;
    }

    public abstract int whack();


    public abstract ImageIcon getImageIcon();


    public void tick() {
        timeRemaining--;
        if (timeRemaining <= 0) {
            hide();
        }
    }
}

