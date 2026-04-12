package com.mogador.mineassistant.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.event.EventHandler;

import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;
import com.mogador.mineassistant.events.HomeEntityStatusChangeEvent;
import com.mogador.mineassistant.managers.PowerableManager;
import com.mogador.mineassistant.utils.Utils;

public class StatusChangeListener implements Listener {
    
    private final JavaPlugin plugin;

    public StatusChangeListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // TODO - To Remove
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!event.isCancelled() && event.getBlockPlaced().getType() == Material.LEVER) {
            PowerableManager.getInstance().add(HomeEntity.SALON, event.getBlockPlaced().getLocation());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.LEVER) return;

        boolean wasPowered = ((Powerable) block.getBlockData()).isPowered();

        Bukkit.getScheduler().runTask(plugin, () -> {
            boolean isPowered = ((Powerable) block.getBlockData()).isPowered();

            if (wasPowered != isPowered) {
                Utils.publishLightChange(HomeEntity.SALON, HomeEntityStatus.valueOf(isPowered), event.getPlayer());
            }
        });
    }

    @EventHandler
    public void onHomeEntityStatusChangeEvent(HomeEntityStatusChangeEvent event) {
        PowerableManager.getInstance().updateStatus(event.getEntity(), event.getStatus());
    }
}
