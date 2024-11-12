package com.eventostec.api.services;

import com.eventostec.api.domain.address.Address;
import com.eventostec.api.domain.address.AddressRequestDTO;
import com.eventostec.api.domain.address.AddressResponseDTO;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.repositories.AddressRepository;
import com.eventostec.api.repositories.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    public AddressResponseDTO create(AddressRequestDTO addressRequestDTO) {
        Address address = new Address();
        Event event = eventRepository.findById(addressRequestDTO.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        address.setUf(addressRequestDTO.uf());
        address.setCity(addressRequestDTO.city());
        address.setEvent(event);

        addressRepository.save(address);

        return new AddressResponseDTO(
                address.getId(),
                address.getUf(),
                address.getCity(),
                address.getEvent()
        );
    }
}
