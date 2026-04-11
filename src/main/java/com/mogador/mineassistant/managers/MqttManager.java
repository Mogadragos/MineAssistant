package com.mogador.mineassistant.managers;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.mogador.mineassistant.callbacks.HomeEntityStatusChangeCallback;
import com.mogador.mineassistant.constants.MqttConstants;

public class MqttManager {

    // Singleton
    private static final MqttManager instance = new MqttManager();
    public static MqttManager getInstance() {
        return instance;
    }
    private MqttManager() {}

    // Configuration values
    private String broker;
    private String clientId;
    private String topic;
    private int qos;
    private boolean automaticReconnect;
    private boolean cleanSession;
    private int connectTimeoutSeconds;

    private JavaPlugin plugin;
    private MqttClient client;

    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;

        FileConfiguration config = plugin.getConfig();
        this.broker = config.getString(MqttConstants.MQTT_BROKER);
        this.clientId = config.getString(MqttConstants.MQTT_CLIENT_ID);
        this.topic = config.getString(MqttConstants.MQTT_TOPIC);
        this.qos = config.getInt(MqttConstants.MQTT_QOS);
        this.connectTimeoutSeconds = config.getInt(MqttConstants.MQTT_CONNECT_TIMEOUT);
        this.automaticReconnect = config.getBoolean(MqttConstants.MQTT_AUTOMATIC_RECONNECT);
        this.cleanSession = config.getBoolean(MqttConstants.MQTT_CLEAN_SESSION);

        try {
            MemoryPersistence persistence = new MemoryPersistence();
            client = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(automaticReconnect);
            options.setCleanSession(cleanSession);
            options.setConnectionTimeout(connectTimeoutSeconds);

            plugin.getLogger().info("Connecting to MQTT broker: " + broker);
            client.connect(options);
            plugin.getLogger().info("MQTT connected");
        } catch(MqttException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to MQTT broker", e);
            return;
        }

        client.setCallback(new HomeEntityStatusChangeCallback(plugin));

        subscribe();
    }
    
    public void publish(String content) {

        if (client == null || !client.isConnected()) {
            plugin.getLogger().warning("MQTT client is not connected; skipping publish");
            return;
        }  

        try {
            MqttMessage message = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
            message.setQos(qos);

            client.publish(topic, message);
            plugin.getLogger().finest("MQTT message published to " + topic);
        } catch(MqttException e) {
            plugin.getLogger().log(Level.SEVERE, "MQTT publish failed", e);
        }

    }

    public void subscribe() {

        if (client == null || !client.isConnected()) {
            plugin.getLogger().warning("MQTT client is not connected; cannot subscribe");
            return;
        }

        try {
            client.subscribe(topic, qos);
            plugin.getLogger().info("MQTT subscribed to topic: " + topic);
        } catch(MqttException e) {
            plugin.getLogger().log(Level.SEVERE, "MQTT subscribe failed", e);
        }

    }

    public void disable() {
        if (client == null) return;

        try {
            client.disconnect();
            client.close();
            plugin.getLogger().info("MQTT disconnected and closed");
        } catch (MqttException e) {
            plugin.getLogger().log(Level.SEVERE, "MQTT disconnect failed", e);
        }
    }
}
