package com.mogador.mineassistant.callbacks;

import java.nio.charset.StandardCharsets;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;
import com.mogador.mineassistant.events.HomeEntityStatusChangeEvent;

public class HomeEntityStatusChangeCallback implements MqttCallback {

    private final JavaPlugin plugin;

    public HomeEntityStatusChangeCallback(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void connectionLost(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        byte[] payload = message.getPayload();
        try {
            JSONObject json = new JSONObject(new String(payload, StandardCharsets.UTF_8));
            if(!JsonConstants.VALUE_SOURCE_MINECRAFT.equals(json.optString(JsonConstants.KEY_SOURCE))) {
                plugin.getLogger().info("Message received");
                HomeEntity entity = HomeEntity.valueOfLabel(json.getString(JsonConstants.KEY_HOME_ENTITY));
                HomeEntityStatus status = HomeEntityStatus.valueOfLabel(json.getString(JsonConstants.KEY_HOME_ENTITY_STATUS));

                Bukkit.getScheduler().runTask(
                    plugin,
                    () -> Bukkit.getPluginManager().callEvent(
                        new HomeEntityStatusChangeEvent(entity, status)
                    )
                );
            } else {
                plugin.getLogger().info("Message received; from minecraft: ignored");
            }
        } catch(JSONException e) {
            String payloadPreview = new String(payload, 0, Math.min(100, payload.length));
            plugin.getLogger().warning("Failed to parse MQTT message on " + topic + " (preview: " + payloadPreview + "...");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Do nothing
        // DM user result
    }
    
}
