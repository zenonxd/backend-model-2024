package com.eventostec.api.domain.address;

import com.eventostec.api.domain.event.Event;

import java.util.UUID;

public record AddressRequestDTO(String uf,
                                String city,
                                UUID eventId) {
}
