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

package com.sktechx.godmusic.personal.common.amqp.service.impl;

import com.sktechx.godmusic.personal.common.amqp.service.AmqpDeliver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 설명 : XXXXXXXXX
 * 
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 26.
 *
 */

@Slf4j
public class AmqpDeliverImpl implements AmqpDeliver {
	private RabbitTemplate 	rabbitTemplate;
	private boolean			shutdown = false;
	private BlockingQueue<DeliveryItem> 	queue = new LinkedBlockingQueue<>();
	private Thread			queueThread;
	
	AmqpDeliverImpl(RabbitTemplate rabbitTemplate)	{
		this.rabbitTemplate = rabbitTemplate;
	}
	
	class DeliveryItem {
		String	exchangeName;
		Object	data;
	}
		
	/* (non-Javadoc)
	 * @see com.sktechx.musicmate.common.amqp.service.impl.AmqpDeliver#request(java.lang.Object)
	 */
	@Override
	public void request(String exchangeName, Object message) {
		DeliveryItem item = new DeliveryItem();
		item.exchangeName = exchangeName;
		item.data = message;
		queue.add(item);
	}

	/* (non-Javadoc)
	 * @see com.sktechx.musicmate.common.amqp.service.impl.AmqpDeliver#start()
	 */
	@Override
	public void start() {		
		queueThread = new Thread()	{
			@Override
			public void run()	{
				consumeQueue();
			}
		};
		queueThread.start();
	}

	/* (non-Javadoc)
	 * @see com.sktechx.musicmate.common.amqp.service.impl.AmqpDeliver#stop()
	 */
	@Override
	public void stop() {
		shutdown = true;
		queueThread.interrupt();
	}


	private	void		consumeQueue()	{
		while( true )	{
			if( shutdown )
				break;
			
			try	{
				DeliveryItem item = queue.take();
				if( item == null )	{
					Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(10));
					continue;
				}
				while( !deliver(item) )	{
					Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(10));
				}
			} catch(InterruptedException e0)	{
				return;
			} catch(Exception ex)	{
				log.error("[{}] fail to consume queue : {}", this.getClass().getSimpleName(), ex.getMessage());
			}
		}
	}
	
	private boolean 	deliver(DeliveryItem message) {
		try {
			rabbitTemplate.convertAndSend(message.exchangeName, "", message.data);
			log.debug("[{}] deliery success : {}", this.getClass().getSimpleName(), message.data.toString());
			return true;
		} catch (Exception ex) {
			log.error("[{}] fail to send : {}", this.getClass().getSimpleName(), ex.getMessage());
			return false;
		}
	}
}
