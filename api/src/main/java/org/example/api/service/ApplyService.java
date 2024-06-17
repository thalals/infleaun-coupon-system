package org.example.api.service;

import org.example.api.domain.Coupon;
import org.example.api.repository.CouponCountRepository;
import org.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
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

        if(count > 100) {
            return;
        }

        couponRepository.save(new Coupon(userId));
    }
}
