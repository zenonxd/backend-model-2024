package com.eventostec.api.domain.coupon;

public record CouponRequestDTO(Integer discount,
                               String code,
                               Long valid) {
}
