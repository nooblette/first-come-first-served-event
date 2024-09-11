package org.example.consumer.consumer;

import org.example.consumer.domain.Coupon;
import org.example.consumer.repository.CouponRepository;
import org.example.consumer.repository.FailedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j // 에러 로깅을 위함
@Component
public class CouponCreatedConsumer {
	private final CouponRepository couponRepository;
	private final FailedEventRepository failedEventRepository;

	public CouponCreatedConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
		this.couponRepository = couponRepository;
		this.failedEventRepository = failedEventRepository;
	}

	@KafkaListener(
		topics = "coupon-create", 	// 토픽 명 지정
		groupId = "group_1", 		// 컨슈머 그룹 ID 지정
		containerFactory = "kafkaListenerContainerFactory" // Listener Container Factory 지정
	)
	public void listener(Long userId) {
		// try {
		// 	couponRepository.save(new Coupon(userId));
		// } catch (Exception e) {
		// 	// 쿠폰 발급에 실패하는 경우
		// 	log.error("failed to create coupon: {}", userId);
		// 	failedEventRepository.save(new FailedEvent(userId));
		// }

		System.out.println("userId = " + userId);
		if(userId % 2 != 0) {
			throw new RuntimeException("쿠폰 발급 실패 : " + userId);
		}
		couponRepository.save(new Coupon(userId));
		// throw new RuntimeException("쿠폰 발급 실패 : " + userId);
	}
}
