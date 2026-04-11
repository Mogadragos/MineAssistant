package com.mogador.mineassistant.enums;

import java.util.Arrays;

public enum HomeEntityStatus {
  ON("on"),
  OFF("off");

  private final String label;

  public String getLabel() {
    return label;
  }

  public boolean toBoolean() {
    return HomeEntityStatus.ON.equals(this);
  }

  HomeEntityStatus(String value) {
    this.label = value;
  }

  public static HomeEntityStatus valueOfLabel(String label) {
    return Arrays.stream(HomeEntityStatus.values())
        .filter(entity -> entity.getLabel().equals(label))
        .findAny()
        .orElseThrow();
  }
}
