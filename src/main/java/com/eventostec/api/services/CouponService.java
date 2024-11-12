package com.eventostec.api.services;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.coupon.CouponResponseDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.repositories.CouponRepository;
import com.eventostec.api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EventRepository eventRepository;

    public CouponResponseDTO associateCouponToEvent(UUID eventId, CouponRequestDTO body) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found."));

        Coupon coupon = new Coupon();
        coupon.setCode(body.code());
        coupon.setDiscount(body.discount());
        coupon.setValid(new Date(body.valid()));
        coupon.setEvent(event);

        couponRepository.save(coupon);

        return new CouponResponseDTO(
                coupon.getId(),
                coupon.getDiscount(),
                coupon.getCode(),
                coupon.getValid(),
                coupon.getEvent()
        );
    }
}
