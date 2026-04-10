package com.mogador.mineassistant.managers;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

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

    private Dictionary<HomeEntity, List<Location>> powerableDict;
    
    public void initialize() {
        this.powerableDict = new Hashtable<HomeEntity, List<Location>>();
    }

    public void updateStatus(HomeEntity entity, HomeEntityStatus status) {
        Iterator<Location> iterator = this.getPowerableList(entity).iterator();
        
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
                iterator.remove();
            }
        }
    }

    public List<Location> getPowerableList(HomeEntity entity) {
        List<Location> powerableList = this.powerableDict.get(entity);
        if (powerableList != null) {
            return powerableList;
        }

        this.powerableDict.put(entity, new ArrayList<>());
        return this.powerableDict.get(entity);
    }
    
}