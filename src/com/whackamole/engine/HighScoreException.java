package com.whackamole.engine;

public class HighScoreException extends Exception {
    public HighScoreException(String message, Throwable cause) {
        super(message, cause);
    }
    public HighScoreException(String message) { super(message); }
}
