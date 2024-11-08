package com.eventostec.api.controllers;

import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventRequestDTO;
import com.eventostec.api.domain.event.EventResponseDTO;
import com.eventostec.api.services.EventService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    //tem que dizer que o Post irá consumir dados do tipo multipart form data
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<EventResponseDTO> create(@RequestParam("title") String title,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam("date") Long date,
                                        @RequestParam("city") String city,
                                        @RequestParam("state") String state,
                                        @RequestParam("remote") Boolean remote,
                                        @RequestParam("eventUrl") String eventUrl,
                                        @RequestParam(value = "image", required = false) MultipartFile image) {

        EventRequestDTO eventRequestDTO = new EventRequestDTO(title, description, date, city, state, remote, eventUrl, image);

        EventResponseDTO createdEvent = eventService.createEvent(eventRequestDTO);

        URI uri = UriComponentsBuilder.fromPath("/api/events/{id}")
                .buildAndExpand(createdEvent.id()).toUri();

        return ResponseEntity.created(uri).body(createdEvent);
    }

    @GetMapping("/events")
    public ResponseEntity<Page<EventResponseDTO>> findAllPaged(Pageable pageable) {

        Page<EventResponseDTO> events = eventService.findAllPaged(pageable);
        return ResponseEntity.ok(events);
    }
}
