package org.example.api.repository;

import org.example.api.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

// 쿠폰 엔티티(Coupon)에 대한 CRUD를 제공
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
