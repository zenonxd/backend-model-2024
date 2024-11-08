package com.eventostec.api.domain.address;

import com.eventostec.api.domain.event.Event;
import jakarta.persistence.*;
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

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 100)
    private String uf;

    @Column(length = 100)
    private String city;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    public Address(String uf, String city) {
        this.uf = uf;
        this.city = city;
    }
}
