package com.mogador.mineassistant.listeners;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.mineassistant.managers.PowerableManager;
import com.mogador.mineassistant.utils.PlayerMessageUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SyncBreakListener implements Listener  {
    
    private final JavaPlugin plugin;

    public SyncBreakListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreakSync(BlockBreakEvent event) {
        if(PowerableManager.getInstance().isSynchronized(event.getBlock())) {
            if(PowerableManager.getInstance().remove(event.getBlock())) {
                String message = "Synchronised block was deleted";
                PlayerMessageUtils.formatAndSend(plugin, event.getPlayer(), message);
                plugin.getLogger().finest(message);
            } else {
                plugin.getLogger().warning("Synchronised block can't be deleted");
            }
        }
    }
    
}
