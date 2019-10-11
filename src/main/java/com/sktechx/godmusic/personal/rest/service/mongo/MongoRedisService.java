/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.mongo;

import com.sktechx.godmusic.lib.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2019. 09. 04.
 */
@Service
@Slf4j
public class MongoRedisService {

    public MongoRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    <T> T executeService(Supplier<T> mongoTask, Supplier<T> asIsTask) {
        if (haveToServiceAsMongo()) {
            return mongoTask.get();
        }
        return asIsTask.get();
    }

    void executeService(VoidFunction mongoTask) {
        if (haveToServiceAsMongo()) {
            mongoTask.apply();
        }
    }

    @Scheduled(fixedRate = 30 * 1000L)
    protected void executeTask() {

        String value = this.redisService.getWithPrefix("api.mgo.enable");
        Boolean haveToServiceAsMongo = StringUtils.isBlank(value) || "1".equals(value);

        if (!haveToServiceAsMongo.equals(valueHolder.get())) {

            log.info("[" + Thread.currentThread().getName() + "] api.mgo.enable 값 변경, 이전 값 : {}, 현재 값 : {}",
                    valueHolder.get(), haveToServiceAsMongo);

            valueHolder.set(haveToServiceAsMongo);

        }

    }

    private Boolean haveToServiceAsMongo() {
        return valueHolder.get();
    }

    private AtomicReference<Boolean> valueHolder = new AtomicReference<>();
    private RedisService redisService;

}
