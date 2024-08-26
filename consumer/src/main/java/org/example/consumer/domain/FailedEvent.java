package org.example.consumer.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

// 쿠폰 발급에 실패한 데이터를 담는 엔티티
@Entity
@NoArgsConstructor
public class FailedEvent {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;

	public FailedEvent(Long userId) {
		this.userId = userId;
	}
}
