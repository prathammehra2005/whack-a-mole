package com.whackamole.model;

import javax.swing.ImageIcon;

public class Bomb extends HoleOccupant {
    private static final long serialVersionUID = 1L;
    public static final int PENALTY = -500;
    private transient ImageIcon icon;

    public Bomb(int lifeTicks, ImageIcon icon) {
        super(lifeTicks);
        this.icon = icon;
    }

    public int whack() {
        hide();
        return PENALTY;
    }

    public ImageIcon getImageIcon() {
        return icon;
    }

    public void setIcon(ImageIcon i) { this.icon = i; }
}
