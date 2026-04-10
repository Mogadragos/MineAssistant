package com.mogador.mineassistant.managers;

public class PersistenceManager {

    // Singleton
    private static PersistenceManager instance;
    public static PersistenceManager getInstance() {
        if (instance == null) {
            instance = new PersistenceManager();
        }
        return instance;
    }
    private PersistenceManager() {}

}
