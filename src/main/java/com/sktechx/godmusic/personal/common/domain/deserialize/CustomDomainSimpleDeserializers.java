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

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;

public class CustomDomainSimpleDeserializers extends SimpleDeserializers {

    @SuppressWarnings("unchecked")
    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
                                                    DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {

        JsonDeserializer<?> deserializer = super.findEnumDeserializer(type, config, beanDesc);

        return deserializer == null && type.isEnum() ? new CodeEnumUndefinedSafeDeserializer(type) : deserializer;

    }

}
