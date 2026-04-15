package com.mogador.mineassistant.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.data.HomeEntityStatusAdapter;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class JsonManager {

    // Singleton
    private static final JsonManager instance = new JsonManager();
    public static JsonManager getInstance() {
        return instance;
    }
    private JsonManager() {}

    // Configuration value
    private String onValue;
    private String offValue;

    private JavaPlugin plugin;
    private Gson gson;

    public void initialize(JavaPlugin plugin) {
        this.plugin = plugin;

        FileConfiguration config = plugin.getConfig();
        this.onValue = config.getString(JsonConstants.HOMEASSISTANT_ON);
        this.offValue = config.getString(JsonConstants.HOMEASSISTANT_OFF);
        initGson();
    }


    public String toJson(Object src) {
        plugin.getLogger().finest("Json Serializing of : ".concat(src.toString()));
        return gson.toJson(src);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        plugin.getLogger().finest("Json Deserializing from : ".concat(json));
        return gson.fromJson(json, classOfT);
    }

    private void initGson() {
        GsonBuilder builder = new GsonBuilder(); 
        builder.registerTypeAdapter(HomeEntityStatus.class, new HomeEntityStatusAdapter(onValue, offValue)); 
        this.gson = builder.create();
    }
}
