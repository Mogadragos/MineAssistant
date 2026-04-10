package com.mogador.mineassistant.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.mogador.mineassistant.MineAssistant;

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

    private MineAssistant plugin;
    private File syncPowerableFile;
    private FileConfiguration syncPowerableConfig;

    public void initialize(MineAssistant plugin) {
        this.plugin = plugin;

        load();
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        syncPowerableFile = new File(plugin.getDataFolder(), "levers.yml");

        if (!syncPowerableFile.exists()) {
            try {
                syncPowerableFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        syncPowerableConfig = YamlConfiguration.loadConfiguration(syncPowerableFile);
    }

    public void persist() {
        try {
            syncPowerableConfig.save(syncPowerableFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getData() {
        return syncPowerableConfig;
    }

}
