package com.whackamole.ui;

import javax.swing.*;
import java.awt.*;

public class HoleButton extends JButton {
    private final int index;

    public HoleButton(int index, Icon holeIcon) {
        super(holeIcon);
        this.index = index;
        setPreferredSize(new Dimension(80, 80));
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    public int getIndex() { return index; }
}
