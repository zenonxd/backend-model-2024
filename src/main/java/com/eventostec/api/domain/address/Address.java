package com.eventostec.api.domain.address;

import com.eventostec.api.domain.event.Event;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    private UUID id;
    private String uf;
    private String city;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
