package com.mogador.mineassistant.enums;

import com.mogador.mineassistant.utils.EnumLabelUtils;

public enum HomeEntityStatus implements EnumLabel {
  ON("on"),
  OFF("off");

  private final String label;

  @Override
  public String getLabel() {
    return label;
  }

  public boolean toBoolean() {
    return ON.equals(this);
  }

  HomeEntityStatus(String value) {
    this.label = value;
  }

  public static HomeEntityStatus valueOfLabel(String label) {
    return EnumLabelUtils.valueOfLabel(HomeEntityStatus.class, label);
  }
}
