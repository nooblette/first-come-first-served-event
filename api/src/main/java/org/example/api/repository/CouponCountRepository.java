package org.example.api.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

// 레디스에 대해 CRUD를 제공하는 리포지토리
@Repository
@RequiredArgsConstructor
public class CouponCountRepository {
	private final RedisTemplate<String, String> redisTemplate;

	// count 1 증가
	public Long increment() {
		return redisTemplate
			.opsForValue()
			// incr 명령어 호출
			.increment("coupon-count");
	}

	// 등록된 key 모두 제거(테스트코드용)
	public void deleteByKey(String key) {
		redisTemplate.delete(key);
	}
}
