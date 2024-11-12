package com.eventostec.api.controllers;

import com.eventostec.api.domain.coupon.Coupon;
import com.eventostec.api.domain.coupon.CouponRequestDTO;
import com.eventostec.api.domain.coupon.CouponResponseDTO;
import com.eventostec.api.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping(value = "/event/{eventId}")
    public ResponseEntity<CouponResponseDTO> associateCouponToEvent(@PathVariable("eventId") UUID eventId,
                                                                    @RequestParam(value = "code", required = true) String code,
                                                                    @RequestParam(value = "discount", required = true) Integer discount,
                                                                    @RequestParam(value = "date", required = false) Long date) {

        CouponRequestDTO couponRequestDTO = new CouponRequestDTO(discount, code, date);

        CouponResponseDTO couponResponseDTO = couponService.associateCouponToEvent(eventId, couponRequestDTO);


        return ResponseEntity.ok(couponResponseDTO);
    }
}
