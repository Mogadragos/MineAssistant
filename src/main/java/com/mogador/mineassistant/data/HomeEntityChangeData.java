package com.mogador.mineassistant.data;

import com.google.gson.annotations.SerializedName;
import com.mogador.mineassistant.constants.JsonConstants;
import com.mogador.mineassistant.enums.HomeEntity;
import com.mogador.mineassistant.enums.HomeEntityStatus;

public class HomeEntityChangeData {
    @SerializedName("entity_id")
    private HomeEntity entity;
    
    @SerializedName("status")
    private HomeEntityStatus status;
    
    @SerializedName("source")
    private String source;

    public HomeEntityChangeData() {} // Mandatory for Gson

    public HomeEntityChangeData(HomeEntity entity, HomeEntityStatus status) {
        this.entity = entity;
        this.status = status;
        this.source = JsonConstants.SOURCE_MINECRAFT;
    }

    public HomeEntity getEntity() {
        return entity;
    }

    public HomeEntityStatus getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }
}
