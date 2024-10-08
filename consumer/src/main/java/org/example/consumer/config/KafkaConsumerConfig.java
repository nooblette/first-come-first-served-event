package org.example.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {
	// Consumer 인스턴스를 생성하는데 필요한 설정값들을 정의
	// 스프링은 간편하게 설정값들을 세팅할 수 있도록 ConsumerFactory 인터페이스를 제공한다.

	// 컨슈머 설정 정보를 세팅하는 ConsumerFactory 빈 등록
	@Bean
	public ConsumerFactory<String, Long> consumerFactory() {
		// 컨슈머 설정 값들을 담을 Map 선언
		Map<String, Object> config = new HashMap<>();

		// 컨슈머 설정 정보 세팅
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); 				// 서버 정보
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "group_1"); 								// 컨슈머 그룹 ID
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class); // key 역직렬화 클래스
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class); // value 역직렬화 클래스

		return new DefaultKafkaConsumerFactory<>(config);
	}

	// 토픽으로부터 메시지를 수신해올 KafkaListener를 만드는 KafkaListenerContainerFactory 빈 등록
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Long> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Long> factory = new ConcurrentKafkaListenerContainerFactory<>();

		// 앞서 세팅한 설정 값을 담는 consuemerFactory를 설정해준다.
		factory.setConsumerFactory(consumerFactory());

		// 에러 핸들러 지정
		factory.setCommonErrorHandler(couponErrorHandler());

		return factory;
	}

	// DLT(Dead Letter Topic)에 발행할 메시지 템플릿
	@Bean
	public KafkaTemplate<String, Long> kafkaTemplate(ProducerFactory<String, Long> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

	// DLT(Dead Letter Topic)에 메시지 발행시 사용할 Producer 설정 세팅
	@Bean
	public ProducerFactory<String, Long> producerFactory() {
		Map<String, Object> config = new HashMap<>();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
		return new DefaultKafkaProducerFactory<>(config);
	}

	// 에러 핸들러 구현
	@Bean
	public CommonErrorHandler couponErrorHandler() {
		DeadLetterPublishingRecoverer recover = new DeadLetterPublishingRecoverer(kafkaTemplate(producerFactory()));

		// 1초(1000MS) 간격으로 2회 재시도
		FixedBackOff fixedBackOff = new FixedBackOff(1000L, 2L);
		return new DefaultErrorHandler(recover, fixedBackOff);
	}
}
