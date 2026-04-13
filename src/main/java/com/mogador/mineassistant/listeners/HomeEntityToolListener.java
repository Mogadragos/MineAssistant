package com.mogador.mineassistant.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
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
    public void onToolPlace(BlockCanBuildEvent event) {
        // TODO - Prevent user to place tool
        // plugin.getLogger().info(event.getBlockData().toString());
    }
}
