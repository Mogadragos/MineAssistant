package com.mogador.mineassistant;

import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.mineassistant.listeners.HomeEntityToolListener;
import com.mogador.mineassistant.listeners.StatusChangeListener;
import com.mogador.mineassistant.listeners.SyncBreakListener;
import com.mogador.mineassistant.managers.PowerableManager;
import com.mogador.mineassistant.managers.HomeEntityToolManager;
import com.mogador.mineassistant.managers.JsonManager;
import com.mogador.mineassistant.managers.MqttManager;
import com.mogador.mineassistant.managers.PersistenceManager;

public class MineAssistant extends JavaPlugin {
    
    @Override
    public void onEnable() {
        // Load config and ensure defaults are written
        saveDefaultConfig();  // creates config.yml from resources if missing
        getLogger().info("Loaded config.yml");

        // Initialize managers
        PersistenceManager.getInstance().initialize(this, "powerable.yml");
        PowerableManager.getInstance().initialize(this);
        JsonManager.getInstance().initialize(this);
        MqttManager.getInstance().initialize(this);
        HomeEntityToolManager.getInstance().initialize(this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new StatusChangeListener(this), this);
        getServer().getPluginManager().registerEvents(new SyncBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new HomeEntityToolListener(this), this);
        
        getLogger().info("MineAssistant has been enabled!");
    }

    @Override
    public void onDisable() {
        MqttManager.getInstance().disable();

        getLogger().info("MineAssistant has been disabled!");
    }
    
}
