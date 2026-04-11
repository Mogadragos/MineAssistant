package com.mogador.mineassistant.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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

    private JavaPlugin plugin;
    private String fileName;
    private File persistenceFile;
    private FileConfiguration persistenceConfig;

    public void initialize(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        load();
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        persistenceFile = new File(plugin.getDataFolder(), fileName);

        if (!persistenceFile.exists()) {
            try {
                persistenceFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        persistenceConfig = YamlConfiguration.loadConfiguration(persistenceFile);
    }

    public FileConfiguration getData() {
        return persistenceConfig;
    }

    public void save() {
        try {
            persistenceConfig.save(persistenceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
