package com.mogador.mineassistant.enums;

import com.google.gson.annotations.SerializedName;

public enum HomeEntity {
  @SerializedName("light.salon")
  SALON,

  @SerializedName("light.buro")
  BUREAU,

  @SerializedName("light.chambre")
  CHAMBRE;
}
