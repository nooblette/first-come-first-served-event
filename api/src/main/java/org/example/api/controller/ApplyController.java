package org.example.api.controller;

import org.example.api.domain.UserRequest;
import org.example.api.service.ApplyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class ApplyController {
	private final ApplyService applyService;

	@PostMapping("/apply")
	public void applyCoupon(@RequestBody UserRequest request) {
		log.info("coupon apply request By userId = {}", request.getUserId());
		applyService.apply(request.getUserId());
	}
}
