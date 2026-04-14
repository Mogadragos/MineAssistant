package com.mogador.mineassistant.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerMessageUtils {

    public static void formatAndSend(JavaPlugin plugin, Player player, String message) {
        player.sendMessage(String.format("§5[%s] - %s", plugin.getName(), message));
    }
    
    private PlayerMessageUtils() {}
}
