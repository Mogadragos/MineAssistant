package com.mogador.mineassistant.data;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class HomeEntityStatusDeserializer implements JsonDeserializer<HomeEntityStatus> {

    @Override
    public HomeEntityStatus deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if(JsonConstants.ON.equals(json.getAsString())) return HomeEntityStatus.ON;
        return HomeEntityStatus.OFF;
    }
    
}
