package org.example.api.service;

import org.assertj.core.api.Assertions;
import org.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void 한번만응모() {
        applyService.apply(1L);
        long count = couponRepository.count();

        Assertions.assertThat(count).isOne();
    }

    @Test
    public void 여러명응모_실패() throws InterruptedException {

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.execute(() -> {
                try {
                    applyService.apply(userId);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        long count = couponRepository.count();
        Assertions.assertThat(count).isEqualTo(100);
    }

    @Test
    public void 여러명응모_Redis() throws InterruptedException {

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.execute(() -> {
                try {
                    applyService.applyToRedis(userId);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        long count = couponRepository.count();
        Assertions.assertThat(count).isEqualTo(100);
    }
}