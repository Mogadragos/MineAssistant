package com.mogador.mineassistant.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;
import com.mogador.mineassistant.events.HomeEntityStatusChangeEvent;
import com.mogador.mineassistant.managers.PowerableManager;
import com.mogador.mineassistant.utils.Utils;

public class StatusChangeListener implements Listener {

    // TODO - To Remove
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!event.isCancelled() && event.getBlockPlaced().getType() == Material.LEVER) {
            PowerableManager.getInstance().add(HomeEntity.SALON, event.getBlockPlaced().getLocation());

            Utils.publishLightChange(HomeEntity.SALON, HomeEntityStatus.ON);
        }
    }

    @EventHandler
    public void onHomeEntityStatusChangeEvent(HomeEntityStatusChangeEvent event) {
        PowerableManager.getInstance().updateStatus(event.getEntity(), event.getStatus());
    }
}
