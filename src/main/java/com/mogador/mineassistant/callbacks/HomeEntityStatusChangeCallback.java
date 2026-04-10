package com.mogador.mineassistant.callbacks;

import org.bukkit.Bukkit;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import com.mogador.mineassistant.MineAssistant;
import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;
import com.mogador.mineassistant.events.HomeEntityStatusChangeEvent;

public class HomeEntityStatusChangeCallback implements MqttCallback {

    private MineAssistant plugin;

    public HomeEntityStatusChangeCallback(MineAssistant plugin) {
        this.plugin = plugin;
    }

    @Override
    public void connectionLost(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        byte[] payload = message.getPayload();
        JSONObject json = new JSONObject(new String(payload, JsonConstants.CHARSET));
        System.out.println("Message received");
        if(!JsonConstants.SOURCE_MINECRAFT.equals(json.optString(JsonConstants.KEY_SOURCE))) {
            HomeEntity entity = HomeEntity.valueOfLabel(json.getString(JsonConstants.KEY_HOME_ENTITY));
            HomeEntityStatus status = HomeEntityStatus.valueOfLabel(json.getString(JsonConstants.KEY_HOME_ENTITY_STATUS));

            Bukkit.getScheduler().runTask(
                plugin,
                () -> Bukkit.getPluginManager().callEvent(
                    new HomeEntityStatusChangeEvent(entity, status)
                )
            );
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("Delivery complete...");
    }
    
}
