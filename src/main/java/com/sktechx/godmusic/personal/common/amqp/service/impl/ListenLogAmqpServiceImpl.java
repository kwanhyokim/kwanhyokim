/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.amqp.service.impl;

import com.sktechx.godmusic.personal.common.amqp.service.ListenLogAmqpDeliver;
import com.sktechx.godmusic.personal.common.amqp.service.ListenLogAmqpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 05. 22
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ListenLogAmqpServiceImpl implements ListenLogAmqpService, CommandLineRunner, DisposableBean {

    @Value("${send.listen.log}")
    private boolean isSend;

    public final static String propExchangeTrackListen = "amqp.exchange.track_listen";

    private String exchangeTrackListen;
    private ListenLogAmqpDeliver amqpDeliver;

    private final Environment environment;
    private final RabbitTemplate newRabbitTemplate;

    @Override
    public void deliverSourcePlay(Object message) {
        log.debug("isSendListenLog={}", isSend);
        if (isSend) {
            amqpDeliver.request(exchangeTrackListen, message);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        amqpDeliver = new ListenLogAmqpDeliverImpl(newRabbitTemplate);
        this.exchangeTrackListen = environment.getProperty(propExchangeTrackListen);
        if (StringUtils.isEmpty(this.exchangeTrackListen)) {
            throw new IllegalArgumentException("not found exchange key name : " + propExchangeTrackListen);
        }

        amqpDeliver.start();
    }

    @Override
    public void destroy() throws Exception {
        if (!ObjectUtils.isEmpty(amqpDeliver)) {
            amqpDeliver.stop();
        }
    }

}
