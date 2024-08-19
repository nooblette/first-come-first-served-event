package org.example.api.service;

import org.example.api.domain.Coupon;
import org.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// 쿠폰에 대한 CRUD를 통해 비즈니스 로직을 제공
public class ApplyService {
	private final CouponRepository couponRepository;

	// 쿠폰 발급 로직
	public void apply(Long userId) {
		// 쿠폰 개수 조회
		long count = couponRepository.count();

		// 쿠폰의 개수가 발급 가능한 개수보다 많은 경우 -> 발급 불가
		if(count > 100) {
			return;
		}

		// 발급이 가능한 경우 ->  쿠폰 새로 생성(발급)
		couponRepository.save(new Coupon(userId));
	}
}
