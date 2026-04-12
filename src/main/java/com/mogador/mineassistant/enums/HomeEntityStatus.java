package com.mogador.mineassistant.enums;

import com.google.gson.annotations.SerializedName;

public enum HomeEntityStatus {
  @SerializedName("on")
  ON(true),

  @SerializedName("off")
  OFF(false);

  private final boolean isOn;

  HomeEntityStatus(boolean isOn) {
    this.isOn = isOn;
  }

  public boolean isOn() {
    return isOn;
  }

  public static HomeEntityStatus valueOf(boolean isOn) {
    return isOn ? ON : OFF;
  }
}
