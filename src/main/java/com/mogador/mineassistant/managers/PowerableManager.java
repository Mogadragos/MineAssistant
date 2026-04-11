package com.mogador.mineassistant.managers;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;

import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class PowerableManager {

    // Singleton
    private static PowerableManager instance;
    public static PowerableManager getInstance() {
        if (instance == null) {
            instance = new PowerableManager();
        }
        return instance;
    }
    private PowerableManager() {}

    private Dictionary<HomeEntity, Set<Location>> powerableDict;
    
    public void initialize() {
        this.powerableDict = new Hashtable<HomeEntity, Set<Location>>();

        for(HomeEntity entity : HomeEntity.values()) {
            List<?> list = PersistenceManager.getInstance().getData().getList(entity.toString());
            if(list != null) {
                Set<Location> powerableSet = getPowerableSet(entity);
                for (Object obj : list) {
                    if (obj instanceof Location loc) {
                        powerableSet.add(loc);
                    }
                }
            }
        }
    }

    public void updateStatus(HomeEntity entity, HomeEntityStatus status) {
        boolean edited = false;
        Iterator<Location> iterator = getPowerableSet(entity).iterator();

        while (iterator.hasNext()) {
            Location loc = iterator.next();
            Block block = loc.getBlock();

            if(block.getBlockData() instanceof Powerable powerable) {
                boolean statusBoolean = status.toBoolean();
                if(powerable.isPowered() ^ statusBoolean) { // Do not update if status doesn't change (^ => XOR)
                    powerable.setPowered(statusBoolean);
                    block.setBlockData(powerable);
                }
            } else {
                edited = true;
                iterator.remove();
            }
        }

        if(edited) {
            persist(entity);
        }
    }

    public void add(HomeEntity entity, Location loc) {
        getPowerableSet(entity).add(loc);
        persist(entity);
    }

    private Set<Location> getPowerableSet(HomeEntity entity) {
        Set<Location> powerableSet = powerableDict.get(entity);
        if (powerableSet != null) {
            return powerableSet;
        }

        powerableDict.put(entity, new HashSet<>());
        return powerableDict.get(entity);
    }

    private void persist(HomeEntity entity) {
        PersistenceManager.getInstance().getData().set(entity.toString(), new ArrayList<>(getPowerableSet(entity)));
        PersistenceManager.getInstance().save();
    }
    
}