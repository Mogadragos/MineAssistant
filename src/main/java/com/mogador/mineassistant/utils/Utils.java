package com.mogador.mineassistant.utils;

import org.bukkit.entity.Player;

import com.google.gson.Gson;
import com.mogador.mineassistant.data.HomeEntityChangeData;
import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;
import com.mogador.mineassistant.managers.MqttManager;

public class Utils {

    public static String prepareLightPayload(HomeEntity entity, HomeEntityStatus status) {

        HomeEntityChangeData data = new HomeEntityChangeData(entity, status);
        Gson gson = new Gson();
        return gson.toJson(data);

    }

    public static void publishLightChange(HomeEntity entity, HomeEntityStatus status, Player player) {

        String content = prepareLightPayload(entity, status);
        MqttManager.getInstance().publish(content, player);

    }
    
    // Add more utility methods here

    private Utils() {}
}
