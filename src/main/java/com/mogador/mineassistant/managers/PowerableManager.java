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
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.plugin.java.JavaPlugin;

import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class PowerableManager {

    // Singleton
    private static final PowerableManager instance = new PowerableManager();
    public static PowerableManager getInstance() {
        return instance;
    }
    private PowerableManager() {}

    private JavaPlugin plugin;
    private final Map<HomeEntity, Set<Location>> powerableMap = new HashMap<>();
    
    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;

        for(HomeEntity entity : HomeEntity.values()) {

            List<?> rawList = PersistenceManager.getInstance().getList(entity.name());

            if(rawList != null) {
                Set<Location> locations = getLocations(entity);
                for (Object obj : rawList) {
                    if (obj instanceof Location loc) {
                        locations.add(loc);
                    } else {
                        plugin.getLogger().log(Level.WARNING, "Invalid entry for {0}: {1}", new Object[]{entity.name(), obj.getClass().getName()});
                    }
                }    
            }
        }
    }

    public void updateStatus(HomeEntity entity, HomeEntityStatus status) {
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
                updatedInvalid = true;
                iterator.remove();
            }
        }

        if(updatedInvalid) persist(entity);
    }

    private void editLoc(HomeEntity entity, Location loc, boolean add) {
        boolean success = add ? getLocations(entity).add(loc) : getLocations(entity).remove(loc);
        String actionWord = add ? "Added" : "Removed";
        
        plugin.getLogger().log(Level.FINEST, "{0} lever for {1}: {2}, existing: {3}", 
            new Object[]{actionWord, entity.name(), loc, success});
        
        if(success) persist(entity);
    }

    public void add(HomeEntity entity, Location loc) {
        editLoc(entity, loc, true);
    }

    public void remove(HomeEntity entity, Location loc) {
        editLoc(entity, loc, false);
    }

    private Set<Location> getLocations(HomeEntity entity) {
        return powerableMap.computeIfAbsent(entity, k -> new HashSet<>());
    }

    private void persist(HomeEntity entity) {
        PersistenceManager.getInstance().setList(entity.name(), new ArrayList<>(getLocations(entity)));
    }
    
}
