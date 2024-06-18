package org.example.consumer.consumer;

import org.example.consumer.domain.Coupon;
import org.example.consumer.repository.CouponRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateConsumer {


    private final CouponRepository couponRepository;

    public CouponCreateConsumer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {

        System.out.println("userId = " +userId);

        couponRepository.save(new Coupon(userId));
    }
}