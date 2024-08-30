package org.example.api.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // ObjectMapper(Jackson 라이브러리)를 사용하는 경우 기본생성자가 필요함
@AllArgsConstructor
public class UserRequest {
	private Long userId;
}
