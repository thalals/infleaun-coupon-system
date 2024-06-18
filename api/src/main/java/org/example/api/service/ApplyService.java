package org.example.api.service;

import org.example.api.domain.Coupon;
import org.example.api.producer.CouponCreateProducer;
import org.example.api.repository.CouponCountRepository;
import org.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, CouponCreateProducer couponCreateProducer) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
    }

    //normal
    public void apply(final Long userId) {
        long count = couponRepository.count();

        if (count > 100) {
            return;
        }

        couponRepository.save(new Coupon(userId));
    }

    //redis
    public void applyToRedis(final Long userId) {
        long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }

        couponRepository.save(new Coupon(userId));
    }

    //redis + kafka
    public void applyToKafka(final Long userId) {
        long count = couponCountRepository.increment();

        if (count > 100) {
            return;
        }
        //kafka 로 전송
        couponCreateProducer.create(userId);
    }

}
