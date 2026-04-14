package com.mogador.mineassistant.enums;

public enum HomeEntityStatus {
  ON(true),

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
