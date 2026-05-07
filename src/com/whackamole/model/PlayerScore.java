package com.whackamole.model;

import java.io.Serializable;
import java.util.Objects;

public class PlayerScore implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int score;

    public PlayerScore() {}

    public PlayerScore(String name, int score) {
        this.name = Objects.requireNonNull(name);
        this.score = score;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public void setName(String name) { this.name = name; }
    public void setScore(int s) { this.score = s; }

    public String toString() {
        return name + ": " + score;
    }
}
