package org.example.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.example.api.domain.UserRequest;
import org.example.api.repository.CouponCountRepository;
import org.example.api.repository.CouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트 후 롤백을 자동으로 수행
class ApplyControllerTest {
	@Autowired
	private MockMvc	mockMvc;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponCountRepository couponCountRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@AfterEach
	void tearDown() {
		// Redis 키 제거
		couponCountRepository.deleteByKey("coupon-count");
		couponCountRepository.deleteByKey("applied-user");
	}

	@Test
	void applyCoupon() throws Exception {
		// 요청 파라미터
		UserRequest request = new UserRequest(1L);

		// API 요청 및 응답 확인
		mockMvc.perform(post("/coupon/apply")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());

		// 쿠폰 발급 확인
		Assertions.assertThat(couponRepository.findById(request.getUserId()))
			.isNotEmpty();
	}
}