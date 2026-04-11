package com.mogador.mineassistant.utils;

import org.json.JSONObject;

import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;
import com.mogador.mineassistant.managers.MqttManager;

public class Utils {

    public static String prepareLightPayload(HomeEntity entity, HomeEntityStatus status) {

        JSONObject json = new JSONObject();
        json.put(JsonConstants.KEY_HOME_ENTITY, entity.getLabel());
        json.put(JsonConstants.KEY_HOME_ENTITY_STATUS, status.getLabel());
        json.put(JsonConstants.KEY_SOURCE, JsonConstants.VALUE_SOURCE_MINECRAFT);
        return json.toString();

    }

    public static void publishLightChange(HomeEntity entity, HomeEntityStatus status) {

        String content = prepareLightPayload(entity, status);
        MqttManager.getInstance().publish(content);

    }
    
    // Add more utility methods here

    private Utils() {}
}
