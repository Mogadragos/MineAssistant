package com.mogador.mineassistant.enums;

import com.mogador.mineassistant.utils.EnumLabelUtils;

public enum HomeEntityStatus implements EnumLabel {
  ON("on", true),
  OFF("off", false);

  private final String label;
  private final boolean isOn;

  @Override
  public String getLabel() {
    return label;
  }

  public boolean isOn() {
    return isOn;
  }

  HomeEntityStatus(String label, boolean isOn) {
    this.label = label;
    this.isOn = isOn;
  }

  public static HomeEntityStatus valueOfLabel(String label) {
    return EnumLabelUtils.valueOfLabel(HomeEntityStatus.class, label);
  }

  public static HomeEntityStatus valueOf(boolean isOn) {
    return isOn ? ON : OFF;
  }
}
