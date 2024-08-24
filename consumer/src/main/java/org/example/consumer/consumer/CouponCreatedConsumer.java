package org.example.consumer.consumer;

import org.example.consumer.domain.Coupon;
import org.example.consumer.repository.CouponRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreatedConsumer {
	private final CouponRepository couponRepository;

	public CouponCreatedConsumer(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	@KafkaListener(
		topics = "coupon-create", 	// 토픽 명 지정
		groupId = "group_1" 		// 컨슈머 그룹 ID 지정
	)
	public void listener(Long userId) {
		couponRepository.save(new Coupon(userId));
	}
}
