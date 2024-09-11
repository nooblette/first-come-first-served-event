package org.example.consumer.consumer;

import org.example.consumer.domain.Coupon;
import org.example.consumer.domain.FailedEvent;
import org.example.consumer.repository.CouponRepository;
import org.example.consumer.repository.FailedEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterTopicConsumer {
	private final CouponRepository couponRepository;
	private final FailedEventRepository failedEventRepository;

	public DeadLetterTopicConsumer(CouponRepository couponRepository, FailedEventRepository failedEventRepository) {
		this.couponRepository = couponRepository;
		this.failedEventRepository = failedEventRepository;
	}

	@KafkaListener(
		topics = "coupon-create.DLT", 	// 토픽 명 지정
		groupId = "group_1", 			// 컨슈머 그룹 ID 지정
		containerFactory = "kafkaListenerContainerFactory" // Listener Container Factory 지정
	)
	public void listener(Long userId) {
		System.out.println("DeadLetterTopicConsumer.listener");
		System.out.println("failed to create coupon = " + userId);
		failedEventRepository.save(new FailedEvent(userId));
		couponRepository.save(new Coupon(userId));

	}
}
