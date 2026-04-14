package com.mogador.mineassistant.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mogador.mineassistant.data.HomeEntityStatusDeserializer;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class JsonUtils {

    public static String toJson(Object src) {
        Gson gson = createGson();
        return gson.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        Gson gson = createGson();
        return gson.fromJson(json, classOfT);
    }

    private static Gson createGson() {
        GsonBuilder builder = new GsonBuilder(); 
        builder.registerTypeAdapter(HomeEntityStatus.class, new HomeEntityStatusDeserializer()); 
        Gson gson = builder.create(); 
        return gson;
    }
    
    private JsonUtils() {}
}
