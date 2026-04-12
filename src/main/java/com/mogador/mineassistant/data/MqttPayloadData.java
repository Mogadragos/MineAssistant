package com.mogador.mineassistant.data;

import com.google.gson.annotations.SerializedName;
import com.mogador.mineassistant.constants.JsonConstants;

public class MqttPayloadData {
    @SerializedName("entity_id")
    private String entityId;
    private String status;
    private String source = JsonConstants.VALUE_SOURCE_MINECRAFT;

    public MqttPayloadData() {}

    public MqttPayloadData(String entityId, String status) {
        this.entityId = entityId;
        this.status = status;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getStatus() {
        return status;
    }

    public String getSource() {
        return source;
    }
}
