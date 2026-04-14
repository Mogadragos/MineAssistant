package com.mogador.mineassistant.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.event.EventHandler;

import com.mogador.mineassistant.enums.HomeEntityStatus;
import com.mogador.mineassistant.events.HomeEntityStatusChangeEvent;
import com.mogador.mineassistant.managers.PowerableManager;
import com.mogador.mineassistant.utils.Utils;

public class StatusChangeListener implements Listener {
    
    private final JavaPlugin plugin;

    public StatusChangeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!Action.RIGHT_CLICK_BLOCK.equals(event.getAction())) return;
        if(Result.DENY.equals(event.useInteractedBlock())) return;

        Block block = event.getClickedBlock();
        if(!PowerableManager.getInstance().isSynchronized(block)) return;

        boolean wasPowered = ((Powerable) block.getBlockData()).isPowered();
        String entity = PowerableManager.getInstance().getEntity(block); // Get entity before scheduler to avoid error if block is destroyed

        Bukkit.getScheduler().runTask(plugin, () -> {
            boolean isPowered = ((Powerable) block.getBlockData()).isPowered();

            if (wasPowered != isPowered) {
                Utils.publishLightChange(entity, HomeEntityStatus.valueOf(isPowered), event.getPlayer());
                PowerableManager.getInstance().updateStatus(entity, isPowered);
            }
        });
    }

    @EventHandler
    public void onHomeEntityStatusChangeEvent(HomeEntityStatusChangeEvent event) {
        PowerableManager.getInstance().updateStatus(event.getEntity(), event.getStatus());
    }
}
