/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.config;

import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 설명 : 청취로그 MQ 분리용 AMQP 설정
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 05. 22
 */
@Slf4j
@Configuration
@EnableRabbit
public class NewRabbitMQConfig implements RecoveryListener {

    @Value("${new.amqp.uri.host}")
    private String host;

    @Value("${new.amqp.uri.vhost}")
    private String vhost;

    @Value("${amqp.credential.user}")
    private String user;

    @Value("${amqp.credential.password}")
    private String password;

    @Bean
    public RabbitTemplate newRabbitTemplate() {
        log.debug("newRabbitTemplate");
        RabbitTemplate template = new RabbitTemplate(newConnectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public ConnectionFactory newConnectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setVirtualHost(vhost);
        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setRecoveryListener(this);
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Override
    public void handleRecovery(Recoverable recoverable) {
        log.warn("MCP(NEW)-RabbitMQ connection recovered");
    }

    @Override
    public void handleRecoveryStarted(Recoverable recoverable) {
        log.warn("MCP(NEW)-RabbitMQ connection recovery started");
    }
}
