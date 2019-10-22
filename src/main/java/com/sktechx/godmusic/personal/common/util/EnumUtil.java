package com.sktechx.godmusic.personal.common.util;

import java.util.Arrays;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;

public class EnumUtil {

    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T valueOfAsNullable(String type, Class<T> enumType) {
        try {
            return Enum.valueOf(enumType, type);
        } catch (IllegalArgumentException e) {
            if (CodeEnum.class.isAssignableFrom(enumType)) {
                return (T) Arrays.stream(enumType.getEnumConstants())
                        .map(codeType -> (CodeEnum) codeType)
                        .filter(codeType -> type.equals(codeType.getCode()))
                        .findFirst()
                        .orElse(null);
            }
        }
        return null;
    }

}
