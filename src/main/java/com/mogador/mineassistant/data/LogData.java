package com.mogador.mineassistant.data;

import java.util.logging.Level;

public class LogData {
    private Level level;
    private String message;
    private Throwable exception;
    
    public LogData(Level level, String message, Throwable exception) {
        this.level = level;
        this.message = message;
        this.exception = exception;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return exception;
    }
}
