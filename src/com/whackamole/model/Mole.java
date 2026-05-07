package com.whackamole.model;

import javax.swing.ImageIcon;

public class Mole extends HoleOccupant {
    private static final long serialVersionUID = 1L;
    public static final int SCORE = 100;
    private transient ImageIcon icon;

    public Mole(int lifeTicks, ImageIcon icon) {
        super(lifeTicks);
        this.icon = icon;
    }

    public int whack() {
        hide();
        return SCORE;
    }

    public ImageIcon getImageIcon() {
        return icon;
    }
    public void setIcon(ImageIcon i) { this.icon = i; }
}
