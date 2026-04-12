package com.mogador.mineassistant.callbacks;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.data.LogData;
import com.mogador.mineassistant.data.MqttPayloadData;
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
        plugin.getLogger().log(Level.SEVERE, "Connection lost : ", e);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        byte[] payload = message.getPayload();
        try {
            Gson gson = new Gson();
            MqttPayloadData data = gson.fromJson(new String(payload, StandardCharsets.UTF_8), MqttPayloadData.class);
            if(!JsonConstants.VALUE_SOURCE_MINECRAFT.equals(data.getSource())) {
                plugin.getLogger().info("Message received");
                HomeEntity entity = HomeEntity.valueOfLabel(data.getEntityId());
                HomeEntityStatus status = HomeEntityStatus.valueOfLabel(data.getStatus());

                Bukkit.getScheduler().runTask(
                    plugin,
                    () -> Bukkit.getPluginManager().callEvent(
                        new HomeEntityStatusChangeEvent(entity, status)
                    )
                );
            } else {
                plugin.getLogger().info("Message received; from minecraft: ignored");
            }
        } catch(JsonSyntaxException e) {
            String payloadPreview = new String(payload, 0, Math.min(100, payload.length));
            plugin.getLogger().warning("Failed to parse MQTT message on " + topic + " (preview: " + payloadPreview + "...");
        }
    }

    @Override 
    public void deliveryComplete(IMqttDeliveryToken token) {
        LogData logData = Optional.ofNullable(token.getException())
                            .map(e -> new LogData(Level.WARNING, "Error delivering message : " + e.getMessage(), e))
                            .orElse(new LogData(Level.FINEST, "Message delivered !", null));

        if(token.getUserContext() instanceof Player player) {
            player.sendMessage(logData.getMessage());
        }

        plugin.getLogger().log(logData.getLevel(), logData.getMessage(), logData.getException());
    }
    
}
