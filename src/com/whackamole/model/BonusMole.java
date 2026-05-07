package com.whackamole.model;

import javax.swing.ImageIcon;

public class BonusMole extends HoleOccupant {
    private static final long serialVersionUID = 1L;
    public static final int SCORE = 1000;
    private transient ImageIcon icon;
    private final int extraSeconds;

    public BonusMole(int lifeTicks, ImageIcon icon, int extraSeconds) {
        super(lifeTicks);
        this.icon = icon;
        this.extraSeconds = extraSeconds;
    }
    public int whack() {
        hide();
        return SCORE;
    }

    public ImageIcon getImageIcon() {
        return icon;
    }

    public int getExtraSeconds() { return extraSeconds; }

    public void setIcon(ImageIcon i) { this.icon = i; }
}
