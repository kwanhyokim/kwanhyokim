/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.config;

import com.google.common.base.Stopwatch;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.client.DisplayClient;
import com.sktechx.godmusic.personal.rest.client.ExternalClient;
import com.sktechx.godmusic.personal.rest.client.MemberClient;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.StreamClient;
import com.sktechx.godmusic.personal.rest.repository.WarmUpMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2020. 02. 11.
 */
@Configuration
@Slf4j(topic = "WarmUp")
public class WarmUpConfig {

    private final ApplicationInfoManager applicationInfoManager;
    private final DisplayClient displayClient;
    private final MemberClient memberClient;
    private final MetaClient metaClient;
    private final StreamClient streamClient;
    private final ExternalClient externalClient;
    private final RedisService redisService;
    private final WarmUpMapper warmUpMapper;

    @Value("${gd.warm-up:true}")
    private boolean requireWarmUp;

    @Value("${spring.datasource.readonly-db.maximum-pool-size:30}")
    private int readOnlyDbMaximumPoolSize;

    @Value("${hystrix.threadpool.default.coreSize:10}")
    private int hystrixThreadPoolCoreSize;

    public WarmUpConfig(@Qualifier("eurekaApplicationInfoManager") ApplicationInfoManager applicationInfoManager,
                        DisplayClient displayClient,
                        MemberClient memberClient,
                        MetaClient metaClient,
                        StreamClient streamClient,
                        ExternalClient externalClient,
                        RedisService redisService,
                        WarmUpMapper warmUpMapper) {

        this.applicationInfoManager = applicationInfoManager;
        this.displayClient = displayClient;
        this.memberClient = memberClient;
        this.metaClient = metaClient;
        this.streamClient = streamClient;
        this.externalClient = externalClient;
        this.redisService = redisService;
        this.warmUpMapper = warmUpMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doWarmUp() {

        if (!requireWarmUp) return;

        try {

            log.info("[WarmUp] 서버 워밍업 시작");

            try (WarmUpExecutor executor = new WarmUpExecutor()) {

                executor.run("Redis", this::warmUpRedis, 64);
                executor.run("Database-readonly", warmUpMapper::select1, readOnlyDbMaximumPoolSize);
                executor.run("DisplayFeignClient", displayClient::ping, getFeignClientWarmingUpTryCounts());
                executor.run("MemberFeignClient", memberClient::ping, getFeignClientWarmingUpTryCounts());
                executor.run("MetaFeignClient", metaClient::ping, getFeignClientWarmingUpTryCounts());
                executor.run("StreamFeignClient", streamClient::ping, getFeignClientWarmingUpTryCounts());
                executor.run("ExternalFeignClient", externalClient::ping, getFeignClientWarmingUpTryCounts());
            }

            log.info("[WarmUp] 서버 워밍업 종료");
        }
        catch (Exception ignore) {
        }
        finally {
            applicationInfoManager.setInstanceStatus(InstanceStatus.UP);
            log.info("[WarmUp] 서버 상태 변경 : {}", InstanceStatus.UP);
        }
    }

    private void warmUpRedis() {

        String meaninglessKey = "__MEANINGLESS__::" + randomAlphanumeric();
        redisService.set(meaninglessKey, "1");
        redisService.get(meaninglessKey);
        redisService.del(meaninglessKey);
    }

    private int[] getFeignClientWarmingUpTryCounts() {
        return new int[]{hystrixThreadPoolCoreSize / 2, hystrixThreadPoolCoreSize, hystrixThreadPoolCoreSize * 2};
    }

    private String randomAlphanumeric() {

        int count = 32;
        StringBuilder strBuilder = new StringBuilder(count);
        String alphaNumeric = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        int anLen = alphaNumeric.length();

        for (int i = 0; i < count; i++) {
            strBuilder.append(alphaNumeric.charAt(random.nextInt(anLen)));
        }

        return strBuilder.toString();
    }

    /**
     * WarmUp 대상 함수를 병렬로 수행하여 자원을 확보하기 위한 Executor.
     *
     * @author Lacti (lactrious@sk.com)
     * @date 2020. 1. 23.
     */
    @Slf4j
    private static class WarmUpExecutor implements Closeable {

        private static final DecimalFormat MILLIS_FORMAT = new DecimalFormat("#.##");

        private static final int DEFAULT_POOL_SIZE = 16;
        private static final int DEFAULT_CLOSE_AWAIT_SECONDS = 5;

        private final ExecutorService executor;
        private final int closeAwaitSeconds;

        public WarmUpExecutor() {
            this(DEFAULT_POOL_SIZE, DEFAULT_CLOSE_AWAIT_SECONDS);
        }

        public WarmUpExecutor(final int poolSize, final int closeAwaitSeconds) {
            this.executor = Executors.newFixedThreadPool(poolSize);
            this.closeAwaitSeconds = closeAwaitSeconds;
        }

        public void run(final String name, final Runnable work, @Nonnull final int... tryCounts) {
            for (int index = 0; index < tryCounts.length; ++index) {
                runOnce(name + "-" + index, work, tryCounts[index]);
            }
        }

        private void runOnce(final String name, final Runnable work, final int repeatCount) {
            final CountDownLatch startSignal = new CountDownLatch(1);
            final CountDownLatch doneSignal = new CountDownLatch(repeatCount);

            final AtomicLong totalElapsed = new AtomicLong(0);
            final AtomicInteger errorCount = new AtomicInteger(0);
            for (int index = 0; index < repeatCount; ++index) {
                executor.execute(() -> {
                    try {
                        // 최대한 동시에 요청하여 자원을 좀 더 확보해본다.
                        startSignal.await();
                        final Stopwatch watch = Stopwatch.createStarted();
                        work.run();
                        totalElapsed.addAndGet(watch.elapsed(TimeUnit.MILLISECONDS));
                    } catch (Exception e) {
                        log.debug("[WarmUp] {} 도중 에러 발생: {}", name, e.getMessage());
                        errorCount.incrementAndGet();
                    }
                    doneSignal.countDown();
                });
            }
            try {
                startSignal.countDown();
                doneSignal.await();
            } catch (InterruptedException e) {
                log.warn("[WarmUp] {} 도중 Interrupted", name);
            }
            log.info("[WarmUp] {} 완료 : {}/{} 총 {}ms 평균 {}ms", name, repeatCount - errorCount.get(), repeatCount,
                    totalElapsed.get(), MILLIS_FORMAT.format(totalElapsed.get() / (double) repeatCount));
        }

        @Override
        public void close() {
            executor.shutdown();
            try {
                executor.awaitTermination(closeAwaitSeconds, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn("[WarmUp] WarmUp ThreadPool 해제 실패", e);
            }
        }
    }
}
