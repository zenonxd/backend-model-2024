package com.eventostec.api.domain.event;

import com.eventostec.api.domain.coupon.CouponResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record EventResponseWithCouponsDTO(UUID id, String title,
                               String description,
                               Date date, String city,
                               String state,
                               String imgUrl,
                               String eventUrl,
                               List<CouponResponseDTO> coupons

) {

}
