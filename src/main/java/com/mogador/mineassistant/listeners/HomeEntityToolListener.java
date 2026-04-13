package com.mogador.mineassistant.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.mineassistant.managers.HomeEntityToolManager;

public class HomeEntityToolListener implements Listener {

    private final String TOOL_KEY = "::";
    private final int TOOL_KEY_LENGTH = TOOL_KEY.length();
    
    private final JavaPlugin plugin;

    public HomeEntityToolListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onRedstoneTorchRenameOnAnvil(PrepareAnvilEvent event) {
        if(event.getResult() == null) return;
        if(!Material.REDSTONE_TORCH.equals(event.getResult().getType())) return;

        String renameText = event.getView().getRenameText();
        if(renameText.length() > TOOL_KEY_LENGTH && renameText.startsWith(TOOL_KEY)) {
            HomeEntityToolManager.getInstance().enableTool(event.getResult(), renameText, TOOL_KEY_LENGTH);
        } else if(HomeEntityToolManager.getInstance().isTool(event.getResult())) {
            HomeEntityToolManager.getInstance().disableTool(event.getResult());
        }
    }

    @EventHandler
    public void onToolPlace(BlockPlaceEvent event) {
        if(HomeEntityToolManager.getInstance().isTool(event.getItemInHand())) {
            event.setBuild(false);
            event.setCancelled(true);
            plugin.getLogger().finest("Can't build an entity tool");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractWithLever(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || !Material.LEVER.equals(block.getType())) return;

        // Cancel event if used item is the tool
        if(event.getItem() != null && HomeEntityToolManager.getInstance().isTool(event.getItem())) {
            event.setUseInteractedBlock(Result.DENY);
        }
    }
}
