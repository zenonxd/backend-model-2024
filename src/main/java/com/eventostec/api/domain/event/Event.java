package com.eventostec.api.domain.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "event")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(length = 100)
    private String title;

    @Column(length = 250)
    private String description;
    private Date date;
    private Boolean remote;

    @Column(length = 100)
    private String img_url;

    @Column(length = 100)
    private String event_url;


}
