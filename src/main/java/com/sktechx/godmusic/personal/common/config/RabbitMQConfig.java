/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
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
 * 설명 : AMQP 설정
 * 
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 15.
 *
 */

@Slf4j
@Configuration
@EnableRabbit
public class RabbitMQConfig implements RecoveryListener {

	@Value("${amqp.uri.host}")
	private String	host;

	@Value("${amqp.uri.vhost}")
	private String	vhost;

	@Value("${amqp.credential.user}")
	private String	user;

	@Value("${amqp.credential.password}")
	private String	password;
	
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setVirtualHost(vhost);
        factory.setHost(host);
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setRecoveryListener(this);
        return factory;
    }

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.RecoveryListener#handleRecovery(com.rabbitmq.client.Recoverable)
	 */
	@Override
	public void handleRecovery(Recoverable recoverable) {
		log.warn("RabbitMQ connection recovered");
	}

	/* (non-Javadoc)
	 * @see com.rabbitmq.client.RecoveryListener#handleRecoveryStarted(com.rabbitmq.client.Recoverable)
	 */
	@Override
	public void handleRecoveryStarted(Recoverable recoverable) {
		log.warn("RabbitMQ connection recovery started");
	}
}
