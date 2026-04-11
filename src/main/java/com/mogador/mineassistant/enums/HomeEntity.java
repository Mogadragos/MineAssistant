package com.mogador.mineassistant.enums;

import com.mogador.mineassistant.utils.EnumLabelUtils;

public enum HomeEntity implements EnumLabel {
  SALON("light.salon"),
  BUREAU("light.buro"),
  CHAMBRE("light.chambre");

  private final String label;

  @Override
  public String getLabel() {
    return label;
  }

  HomeEntity(String value) {
    this.label = value;
  }

  public static HomeEntity valueOfLabel(String label) {
    return EnumLabelUtils.valueOfLabel(HomeEntity.class, label);
  }
}
