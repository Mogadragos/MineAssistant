package com.mogador.mineassistant.enums;

import java.util.Arrays;

public enum HomeEntity {
  SALON("light.salon"),
  BUREAU("light.buro"),
  CHAMBRE("light.chambre");

  public final String label;

  public String getLabel() {
    return label;
  }

  HomeEntity(String value) {
    this.label = value;
  }

  public static HomeEntity valueOfLabel(String label) {
    return Arrays.stream(HomeEntity.values())
        .filter(entity -> entity.getLabel().equals(label))
        .findAny()
        .orElseThrow();
  }
}