package com.whackamole.engine;

public class InvalidGameStateException extends RuntimeException {
    public InvalidGameStateException(String message) {
        super(message);
    }
}
