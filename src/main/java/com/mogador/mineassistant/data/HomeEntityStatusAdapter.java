package com.mogador.mineassistant.data;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class HomeEntityStatusAdapter extends TypeAdapter<HomeEntityStatus> {

    @Override
    public HomeEntityStatus read(JsonReader in) throws IOException {
        if (JsonToken.NULL.equals(in.peek())) {
            in.nextNull();
            return HomeEntityStatus.OFF;
        }

        return JsonConstants.ON.equals(in.nextString()) ? HomeEntityStatus.ON : HomeEntityStatus.OFF;
    }

    @Override
    public void write(JsonWriter out, HomeEntityStatus value) throws IOException {
        out.value(HomeEntityStatus.ON.equals(value) ? JsonConstants.ON : JsonConstants.OFF);
    }
    
}
