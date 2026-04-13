package com.mogador.mineassistant.managers;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class HomeEntityToolManager {

    // Singleton
    private static final HomeEntityToolManager instance = new HomeEntityToolManager();
    public static HomeEntityToolManager getInstance() {
        return instance;
    }
    private HomeEntityToolManager() {}

    // Constants
    private final String COLOR = "§6"; // Gold
    private final String LORE_FIRST_LINE  = "§7Control tool for : ";
    private final String LORE_SECOND_LINE = "§7Right click on a lever";
    private final String LORE_THIRD_LINE  = "§7to enable/disable";

    private JavaPlugin plugin;
    private NamespacedKey key;

    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "entity_id");
    }

    public void enableTool(ItemStack tool, String name, int toolKeyLength) {
        ItemMeta meta = tool.getItemMeta();

        meta.setDisplayName(COLOR.concat(name));

        String entityId = name.substring(toolKeyLength);
        meta.setLore(buildLore(entityId));
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, entityId);
        tool.setItemMeta(meta);

        plugin.getLogger().finest("Tool for " + entityId + " enabled");
    }

    public boolean isTool(ItemStack tool) {
        return tool.getItemMeta().getPersistentDataContainer().has(key);
    }

    public void disableTool(ItemStack tool) {
        ItemMeta meta = tool.getItemMeta();

        meta.setLore(null);
        meta.getPersistentDataContainer().remove(key);
        tool.setItemMeta(meta);

        plugin.getLogger().finest("Tool disabled");
    }

    private List<String> buildLore(String entityId) {
        String custom_first_line = LORE_FIRST_LINE.concat(entityId);
        return List.of(custom_first_line, LORE_SECOND_LINE, LORE_THIRD_LINE);
    }
}
