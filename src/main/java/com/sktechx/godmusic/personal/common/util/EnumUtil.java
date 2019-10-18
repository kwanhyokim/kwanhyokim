package com.sktechx.godmusic.personal.common.util;

public class EnumUtil {

    public static <T extends Enum<T>> T valueOfAsNullable(String type, Class<T> enumType) {

        try {
            return Enum.valueOf(enumType, type);
        } catch (IllegalArgumentException ignore) {}

        return null;

    }

}
