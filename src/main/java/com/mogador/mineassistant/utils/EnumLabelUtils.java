package com.mogador.mineassistant.utils;

import java.util.Arrays;

import com.mogador.mineassistant.enums.EnumLabel;

public class EnumLabelUtils {
    private EnumLabelUtils() {}

    public static <T extends EnumLabel> T valueOfLabel(
        Class<T> enumClass,
        String label
    ) {
        return Arrays.stream(enumClass.getEnumConstants())
            .filter(e -> e.getLabel().equals(label))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("No enum constant with label: " + label + " inside " + enumClass.getName()));
    }
}
