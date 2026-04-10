package com.mogador.mineassistant;

import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.mineassistant.listeners.StatusChangeListener;
import com.mogador.mineassistant.managers.PowerableManager;
import com.mogador.mineassistant.managers.MqttManager;

public class MineAssistant extends JavaPlugin {
    
    @Override
    public void onEnable() {
        
        // Initialize managers
        PowerableManager.getInstance().initialize();
        MqttManager.getInstance().initialize(this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new StatusChangeListener(), this);
        
        getLogger().info("MineAssistant has been enabled!");
    }

    @Override
    public void onDisable() {
        MqttManager.getInstance().disable();

        getLogger().info("MineAssistant has been disabled!");
    }
    
}