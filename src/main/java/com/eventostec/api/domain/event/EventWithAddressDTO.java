package com.eventostec.api.domain.event;

import java.util.Date;
import java.util.UUID;

public record EventWithAddressDTO(UUID eventId,
                                  String title,
                                  String description,
                                  Date date,
                                  String city,
                                  String uf,
                                  Boolean remote,
                                  String eventUrl,
                                  String imgUrl) {
}
