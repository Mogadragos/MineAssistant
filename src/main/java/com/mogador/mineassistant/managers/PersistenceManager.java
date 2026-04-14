package com.mogador.mineassistant.managers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PersistenceManager {

    // Singleton
    private static final PersistenceManager instance = new PersistenceManager();
    public static PersistenceManager getInstance() {
        return instance;
    }
    private PersistenceManager() {}

    private final char DOT = '.';
    private final char ESCAPING_CHAR = '¤';

    private JavaPlugin plugin;
    private File persistenceFile;
    private FileConfiguration config;

    public void initialize(JavaPlugin plugin, String fileName) {
        this.plugin = plugin;

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        persistenceFile = new File(plugin.getDataFolder(), fileName);

        if (!persistenceFile.exists()) {
            try {
                persistenceFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create persistence file: " + e.getMessage());
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(persistenceFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Set<String> getKeys() {
        return config.getKeys(false).stream().map(this::unescapeDot).collect(Collectors.toSet());
    }

    public List<?> getList(String key) {
        return config.getList(escapeDot(key));
    }

    public void setList(String key, List<?> list) {
        config.set(escapeDot(key), list);
        save();
    }

    public void save() {
        try {
            config.save(persistenceFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save persistence file: " + e.getMessage());
        }
    }

    public String escapeDot(String str) {
        return str.replace(DOT, ESCAPING_CHAR);
    }

    public String unescapeDot(String str) {
        return str.replace(ESCAPING_CHAR, DOT);
    }
}
