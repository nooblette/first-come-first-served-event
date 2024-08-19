package org.example.api.service;

import static org.assertj.core.api.Assertions.*;

import org.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

		// 쿠폰 1개가 정상적으로 발급되었는지 검증
		assertThat(count).isEqualTo(1);
	}
}