package org.example.api.service;

import org.example.api.domain.Coupon;
import org.example.api.producer.CouponCreateProducer;
import org.example.api.repository.AppliedUserRepository;
import org.example.api.repository.CouponCountRepository;
import org.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// 쿠폰에 대한 CRUD를 통해 비즈니스 로직을 제공
public class ApplyService {
	private final CouponRepository couponRepository;
	private final CouponCountRepository couponCountRepository;
	private final CouponCreateProducer couponCreateProducer;
	private final AppliedUserRepository applieduserRepository;

	// 쿠폰 발급 로직
	public void apply(Long userId) {
		// 쿠폰을 발급받은 사용자인지  검증
		Long apply = applieduserRepository.add(userId);

		// 이미 쿠폰을 발급받은 사용자라면 쿠폰 미발급
		if(apply != 1) {
			return;
		}

		// 쿠폰발급 로직 수행
		// 쿠폰 개수 조회(mysql)
		// long count = couponRepository.count();

		// 쿠폰 개수 조회 로직을 레디스의 incr 명령어를 호출하도록 변경한다.
		// 즉 쿠폰 발급 전에 발급된 쿠폰의 개수를 1 증가하고 그 개수를 확인한다.
		Long increment = couponCountRepository.increment();

		// 쿠폰의 개수가 발급 가능한 개수보다 많은 경우 -> 발급 불가
		if(increment > 100) {
			return;
		}

		// 발급이 가능한 경우 ->  쿠폰 새로 생성(발급)
		// couponRepository.save(new Coupon(userId)); // 쿠폰 발급(mysql)

		// 쿠폰을 직접 DB에 생성하지 않고 카프카 토픽에 userId를 전송한다.
		couponCreateProducer.create(userId);
	}
}
