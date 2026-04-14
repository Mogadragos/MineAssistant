package com.mogador.mineassistant.listeners;

import org.bukkit.event.block.BlockBreakEvent;

import com.mogador.mineassistant.managers.PowerableManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SyncBreakListener implements Listener  {

    @EventHandler
    public void onBlockBreakSync(BlockBreakEvent event) {
        if(PowerableManager.getInstance().isSynchronized(event.getBlock())) {
            PowerableManager.getInstance().remove(event.getBlock());
        }
    }
    
}
