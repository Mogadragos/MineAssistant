package com.mogador.mineassistant.managers;

import java.io.UnsupportedEncodingException;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.mogador.mineassistant.MineAssistant;
import com.mogador.mineassistant.callbacks.HomeEntityStatusChangeCallback;
import com.mogador.mineassistant.constants.JsonConstants;

public class MqttManager {

    // Singleton
    private static MqttManager instance;
    public static MqttManager getInstance() {
        if (instance == null) {
            instance = new MqttManager();
        }
        return instance;
    }
    private MqttManager() {}

    private final String BROKER = "tcp://localhost:1883";
    private final String CLIENT_ID = "MinecraftServer";
    private final String TOPIC = "homeassistant/light/state";
    private final int QOS = 0;

    private MqttClient client;

    public void initialize(MineAssistant plugin) {

        try {
            MemoryPersistence persistence = new MemoryPersistence();
            client = new MqttClient(BROKER, CLIENT_ID, persistence);

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            System.out.println("Connecting to broker: "+BROKER);
            client.connect(options);
            System.out.println("Connected");
        } catch(MqttException me) {
            me.printStackTrace();
        }

        client.setCallback(new HomeEntityStatusChangeCallback(plugin));

        subscribe();
    }
    
    public void publish(String content) {

        if (!client.isConnected()) {
            System.out.println("MQTT client is not connected");
            return;
        }  

        try {
            MqttMessage message = new MqttMessage(content.getBytes(JsonConstants.CHARSET));
            message.setQos(QOS);

            client.publish(TOPIC, message);
            System.out.println("Message published");
        } catch(MqttException me) {
            me.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void subscribe() {

        try {
            client.subscribe(TOPIC, QOS);
            System.out.println("Topic subscribed");
        } catch(Exception me) {
            me.printStackTrace();
        }

    }

    public void disable() {
        try {
            client.disconnect();
            client.close();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }
}
