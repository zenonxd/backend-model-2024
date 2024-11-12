package com.eventostec.api.domain.address;

import com.eventostec.api.domain.event.Event;

import java.util.UUID;

public record AddressResponseDTO(UUID id,
                                 String uf,
                                 String city,
                                 Event event) {
}
