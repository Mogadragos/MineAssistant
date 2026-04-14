package com.mogador.mineassistant.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
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
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;

        for(String entity : PersistenceManager.getInstance().getKeys()) {

            List<?> rawList = PersistenceManager.getInstance().getList(entity);

            if(rawList != null) {
                Set<Location> locations = getLocations(entity);
                for (Object obj : rawList) {
                    if (obj instanceof Location loc) {
                        locations.add(loc);
                    } else {
                        plugin.getLogger().warning(String.format("Invalid entry for %s: %s", entity, obj.getClass().getName()));
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
                // Kept to avoid undeleted powerable
                updatedInvalid = true;
                iterator.remove();
                plugin.getLogger().warning(String.format("Removal that shouldn't exist have occured for %s at %s", entity, loc.toString()));
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

    // Use remove(String entity, Block block) if possible
    public boolean remove(Block block) {
        return remove(getEntity(block), block);
    }

    public boolean isSynchronized(Block block) {
        return powerableMap.values().stream()
        .anyMatch(v -> v.contains(block.getLocation()));
    }
    
    public String getEntity(Block block) {
        return powerableMap.entrySet().stream()
        .filter(e -> e.getValue().contains(block.getLocation()))
        .map(Entry::getKey)
        .findAny()
        .orElse(null);
    }

    private Set<Location> getLocations(String entity) {
        return powerableMap.computeIfAbsent(entity, k -> new HashSet<>());
    }

    private boolean edit(String entity, Block block, boolean add) {
        String actionWord = add ? "Add" : "Remove";

        Set<Location> locations = getLocations(entity);
        boolean success = add ? locations.add(block.getLocation()) : locations.remove(block.getLocation());
        
        plugin.getLogger().finest(String.format("%s lever for %s: %s, success: %b", actionWord, entity, block.getLocation(), success));
        
        if(success) persist(entity);

        return success;
    }

    private void persist(String entity) {
        PersistenceManager.getInstance().setList(entity, new ArrayList<>(getLocations(entity)));
    }
}
