package org.example.api.service;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	@Test
	public void 여러명응모() throws InterruptedException {
		// 동시에 여러 요청이 들어오기 때문에 멀티쓰레드를 사용한다.
		// 1000개의 요청이 동시에 들어오는 경우를 가정한다.
		int threadCount = 1000;

		// ExecutorService : 병렬 작업을 간단하게 수행하도록 도와주는, 스레드풀을 관리하는 자바 API
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		// CountDownLatch :
		// - 여러 스레드가 특정 시점까지 대기하거나, 특정 조건이 만족될 때까지 실행을 지연하는 메커니즘을 제공
		// - 다른 스레드에서 진행중인 작업이 모두 완료할 떄까지 대기하는데 사용한다.
		CountDownLatch latch = new CountDownLatch(threadCount); // count 값을 threadCount 값으로 초기화
		for(int i = 0; i < threadCount; i++) {
			long userId = i;
			executorService.execute(() -> {
				try {
					applyService.apply(userId);
				} finally {
					// count 값을 1 감소
					latch.countDown();
				}

			});
		}

		// await() 이후 로직은 count 값이 0이 되고 나서 실행된다.
		latch.await();

		// 컨슈머가 메시지를 처리하는 동안 잠시 대기
		Thread.sleep(10000);

		// 100개의 쿠폰이 생성된 것을 예상
		long count = couponRepository.count();
		assertThat(count).isEqualTo(100);
	}

	@Test
	public void 한명당_한개의쿠폰만_발급() throws InterruptedException {
		// 동시에 여러 요청이 들어오기 때문에 멀티쓰레드를 사용한다.
		// 1000개의 요청이 동시에 들어오는 경우를 가정한다.
		int threadCount = 1000;

		// ExecutorService : 병렬 작업을 간단하게 수행하도록 도와주는, 스레드풀을 관리하는 자바 API
		ExecutorService executorService = Executors.newFixedThreadPool(32);

		// CountDownLatch :
		// - 여러 스레드가 특정 시점까지 대기하거나, 특정 조건이 만족될 때까지 실행을 지연하는 메커니즘을 제공
		// - 다른 스레드에서 진행중인 작업이 모두 완료할 떄까지 대기하는데 사용한다.
		CountDownLatch latch = new CountDownLatch(threadCount); // count 값을 threadCount 값으로 초기화
		for(int i = 0; i < threadCount; i++) {
			executorService.execute(() -> {
				try {
					// 1이라는 유저가 1000번의 요청을 보내도 쿠폰은 한 개만 발급되는지 확인한다.
					applyService.apply(1L);
				} finally {
					// count 값을 1 감소
					latch.countDown();
				}

			});
		}

		// await() 이후 로직은 count 값이 0이 되고 나서 실행된다.
		latch.await();

		// 컨슈머가 메시지를 처리하는 동안 잠시 대기
		Thread.sleep(10000);

		long count = couponRepository.count();
		assertThat(count).isEqualTo(1); // 쿠폰은 1개만 생성
	}
}