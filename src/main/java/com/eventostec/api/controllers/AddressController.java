package com.eventostec.api.controllers;


import com.eventostec.api.domain.address.AddressRequestDTO;
import com.eventostec.api.domain.address.AddressResponseDTO;
import com.eventostec.api.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> create(@RequestParam("uf") String uf,
                                                     @RequestParam("city") String city,
                                                     @RequestParam("eventId") UUID eventId) {

        AddressRequestDTO addressRequestDTO = new AddressRequestDTO(uf, city, eventId);

        AddressResponseDTO createdAddress = addressService.create(addressRequestDTO);

        URI uri = UriComponentsBuilder.fromPath("/api/address/{id}")
                .buildAndExpand(createdAddress.id()).toUri();

        return ResponseEntity.created(uri).body(createdAddress);
    }
}
