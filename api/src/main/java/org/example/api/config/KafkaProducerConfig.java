package org.example.api.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {
	// 프로듀서 인스턴스를 생성하는데 필요한 설정값을 작성한다.
	// 스프링은 ProducerFactory 인터페이스를 제공하여 손쉽게 설정값을 입력할 수 있다.
	@Bean
	public ProducerFactory<String, Long> producerFactory() {
		// ProducerFactory<String, Long> : key는 Sring, value는 Long으로 지정한다.
		// 설정 값을 담아줄 map을 변수로 선언한다.
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

		return new DefaultKafkaProducerFactory<>(config);
	}

	// 카프카 토픽에 메시지를 발행하는데 사용할 카프카 템플릿
	@Bean
	public KafkaTemplate<String, Long> kafkaTemplate() {
		// 카프카 템플릿을 작성할때 위에서 작성한 설정값을 담아준다.
		// 프로듀서(Producer)는 이 카프카 템플릿을 사용해서 토픽에 메시지를 발행한다.
		return new KafkaTemplate<>(producerFactory());
	}
}
