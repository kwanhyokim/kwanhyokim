/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.amqp.service.impl;

import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpDeliver;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 설명 : AMQP 서비스 구현체
 *
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 15.
 */
@Component
public class AmqpServiceImpl implements AmqpService, CommandLineRunner, DisposableBean {
    public final static String propExchangeTrackListen = "amqp.exchange.track_listen";
    public final static String propExchangeUserEvent = "amqp.exchange.user_event";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment environment;

    private AmqpDeliver amqpDeliver;

    private String exchangeTrackListen;
    private String exchangeUserEvent;

    /**
     * 곡, 비디오 청취 로그 발송
     */
    @Override
    public void deliverSourcePlay(Object message) {
        amqpDeliver.request(exchangeTrackListen, message);
    }

    /**
     * UserEvent 발송
     */
    @Override
    public void deliverUserEvent(UserEvent data) {
        amqpDeliver.request(exchangeUserEvent, data);
    }

    @Override
    public void run(String... args) throws Exception {
        amqpDeliver = new AmqpDeliverImpl(rabbitTemplate);

        this.exchangeTrackListen = environment.getProperty(propExchangeTrackListen);
        if (this.exchangeTrackListen == null) {
            throw new IllegalArgumentException("not found exchange key name : " + propExchangeTrackListen);
        }

        this.exchangeUserEvent = environment.getProperty(propExchangeUserEvent);
        if (this.exchangeUserEvent == null) {
            throw new IllegalArgumentException("not found exchange key name : " + propExchangeUserEvent);
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
