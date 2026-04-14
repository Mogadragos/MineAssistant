package com.mogador.mineassistant.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.mineassistant.enums.HomeEntityStatus;

public class PowerableManager {

    // Singleton
    private static final PowerableManager instance = new PowerableManager();
    public static PowerableManager getInstance() {
        return instance;
    }
    private PowerableManager() {}

    private JavaPlugin plugin;
    private final Map<String, Set<Location>> powerableMap = new HashMap<>();
    private NamespacedKey key;
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;
        this.key = new NamespacedKey(plugin, "entity_id");

        for(String entity : PersistenceManager.getInstance().getKeys()) {

            List<?> rawList = PersistenceManager.getInstance().getList(entity);

            if(rawList != null) {
                Set<Location> locations = getLocations(entity);
                for (Object obj : rawList) {
                    if (obj instanceof Location loc) {
                        locations.add(loc);
                    } else {
                        plugin.getLogger().log(Level.WARNING, "Invalid entry for {0}: {1}", new Object[]{entity, obj.getClass().getName()});
                    }
                }    
            }
        }
    }

    public void updateStatus(String entity, HomeEntityStatus status) {
        boolean updatedInvalid = false;
        Iterator<Location> iterator = getLocations(entity).iterator();

        while (iterator.hasNext()) {
            Location loc = iterator.next();
            Block block = loc.getBlock();

            if(block.getBlockData() instanceof Powerable powerable) {
                boolean desired = status.isOn();
                if(powerable.isPowered() != desired) {
                    powerable.setPowered(desired);
                    block.setBlockData(powerable);
                }
            } else {
                // TODO - Will be done when block is destroyed 
                updatedInvalid = true;
                iterator.remove();
            }
        }

        if(updatedInvalid) persist(entity);
    }

    public boolean add(String entity, Block block) {
        return edit(entity, block, true);
    }

    public boolean remove(String entity, Block block) {
        return edit(entity, block, false);
    }

    private Set<Location> getLocations(String entity) {
        return powerableMap.computeIfAbsent(entity, k -> new HashSet<>());
    }

    private boolean edit(String entity, Block block, boolean add) {
        String actionWord = add ? "Added" : "Removed";

        Set<Location> locations = getLocations(entity);
        boolean success = add ? locations.add(block.getLocation()) : locations.remove(block.getLocation());
        
        plugin.getLogger().log(Level.FINEST, "{0} lever for {1}: {2}, success: {3}", 
            new Object[]{actionWord, entity, block.getLocation(), success});
        
        if(success) {
            PersistentDataContainer container = block.getChunk().getPersistentDataContainer();
            NamespacedKey blockKey = genBlockKey(block);
            if(add) container.set(blockKey, PersistentDataType.STRING, entity);
            else container.remove(blockKey);

            persist(entity);
        }

        return success;
    }

    private void persist(String entity) {
        PersistenceManager.getInstance().setList(entity, new ArrayList<>(getLocations(entity)));
    }
    
    // PersistentDataContainer

    public boolean isSynchronized(Block block) {
        return block.getChunk().getPersistentDataContainer().has(genBlockKey(block));
    }
    
    public String getEntity(Block block) {
        return block.getChunk().getPersistentDataContainer().get(genBlockKey(block), PersistentDataType.STRING);
    }

    private NamespacedKey genBlockKey(Block block) {
        return new NamespacedKey(key.toString(), locationXYZToString(block));
    }

    private String locationXYZToString(Block block) {
        return String.format("%d.%d.%d", block.getX(), block.getY(), block.getZ());
    }
}
