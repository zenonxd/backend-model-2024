package com.eventostec.api.domain.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public record EventRequestDTO(String title,
                              String description,
                              Long date,
                              String city,
                              String state,
                              Boolean remote,
                              String eventUrl,
                              MultipartFile image) {
}
