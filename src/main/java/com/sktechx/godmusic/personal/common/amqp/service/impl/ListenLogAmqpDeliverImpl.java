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
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 05. 22
 */
@RequiredArgsConstructor
@Slf4j
public class ListenLogAmqpDeliverImpl implements ListenLogAmqpDeliver {

    private final RabbitTemplate listenLogRabbitTemplate;

    private BlockingQueue<DeliverItem> queue = new LinkedBlockingDeque<>();
    private Thread queueThread;
    private boolean shutdown = false;

    @NoArgsConstructor
    class DeliverItem {
        String exchangeName;
        Object data;

        public DeliverItem(String exchangeName, Object data) {
            this.exchangeName = exchangeName;
            this.data = data;
        }
    }

    @Override
    public void request(String exchangeName, Object message) {
        queue.add(new DeliverItem(exchangeName, message));
    }

    @Override
    public void start() {
        queueThread = new Thread() {
            @Override
            public void run() {
                consumeQueue();
            }
        };
        queueThread.start();
    }

    @Override
    public void stop() {
        shutdown = true;
        queueThread.interrupt();
    }

    private void consumeQueue() {
        while (true) {
            if (shutdown) {
                break;
            }

            try {
                DeliverItem item = queue.take();
                if (item == null) {
                    log.info("amqp deliver wait caused by item is null");
                    Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(3));
                    continue;
                }

                while (!deliver(item)) {
                    Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(3));
                }

            } catch (InterruptedException i0) {
                return;
            } catch (Exception e) {
                log.error("[{}] fail to consume queue : {}", this.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

    private boolean deliver(DeliverItem message) {
        try {
            listenLogRabbitTemplate.convertAndSend(message.exchangeName, "", message.data);
            log.debug("[{}] deliver success : {}", this.getClass().getSimpleName(), message.data.toString());
            return true;

        } catch (Exception ex) {
            log.error("[{}] fail to send : {}", this.getClass().getSimpleName(), ex.getMessage());
            return false;
        }
    }

}
