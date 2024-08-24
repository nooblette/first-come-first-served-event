package org.example.consumer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

// Database의 Coupon 테이블과 매칭될 엔티티 클래스
@Entity
@NoArgsConstructor
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // coupon id

	@Getter
	private Long userId; // 쿠폰을 발급받은 사용자의 id

	public Coupon(Long userId) {
		this.userId = userId;
	}
}
