/*
 * Copyright (c) 2019 Dreamus Company.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.common.domain.deserialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.sktechx.godmusic.personal.common.util.EnumUtil;

public class CodeEnumUndefinedSafeDeserializer <T extends Enum<T>> extends JsonDeserializer<T> {

    CodeEnumUndefinedSafeDeserializer(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return EnumUtil.valueOfAsNullable(p.getText(), enumType);
    }

    private Class<T> enumType;

}
